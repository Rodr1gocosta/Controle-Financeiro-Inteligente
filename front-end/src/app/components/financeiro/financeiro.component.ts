import { Component, ViewChild } from '@angular/core';
import { FinanceiroService } from '../service/financeiro.service';
import { Planning } from 'src/app/shared/model/planning';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Categories } from 'src/app/shared/model/categories';
import { MatDialog } from '@angular/material/dialog';
import { FinanceiroCrudComponent } from './financeiro-crud/financeiro-crud.component';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

@Component({
  selector: 'app-financeiro',
  templateUrl: './financeiro.component.html',
  styleUrls: ['./financeiro.component.scss'],
})
export class FinanceiroComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatTable) table!: MatTable<any>;
  displayedColumns: string[] = ['category', 'tipo', 'planned', 'status', 'descricao', 'acao'];
  dataSource: any;
  showTable: boolean = true;

  calendarioCustomButton!: string;
  month!: string;
  year!: number;
  isCalendarOpen = false;
  months = [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ];

  planning!: Planning;
  categories: Categories[] = [];


  constructor(private financeiroService: FinanceiroService,
              public dialog: MatDialog,
              private messageOperationService: MessageOperationService) { }

  ngOnInit() {
    this.findPlanningByDateCurrent();
  }

  createPlanning(element: Planning | null) {
    const dialogRef = this.dialog.open(FinanceiroCrudComponent, {
      width: '100%',
      maxHeight: '78vh',
      data: {
        month: this.months.indexOf(this.month) + 1,
        year: this.year
      }
    });
  }

  decreasesYear() {
    if (this.isCalendarOpen) {
      this.year = this.year - 1;
    } else {
      const monthIndex = this.months.indexOf(this.month);
      const previousMonthIndex = (monthIndex - 1 + 12) % 12;

      this.month = this.months[previousMonthIndex];

      if (this.month === 'Dezembro') {
        this.year = this.year - 1;
      }
      this.findByPlanning(previousMonthIndex + 1, this.year);
    }
  }

  incrementYear() {
    if (this.isCalendarOpen) {
      this.year = this.year + 1;
    } else {
      const monthIndex = this.months.indexOf(this.month);
      const previousMonthIndex = (monthIndex + 1 + 12) % 12;

      this.month = this.months[previousMonthIndex];

      if (this.month === 'Janeiro') {
        this.year = this.year + 1;
      }
      this.findByPlanning(previousMonthIndex + 1, this.year);
    }
  }

  toggleCalendar() {
    this.isCalendarOpen = !this.isCalendarOpen;
  }

  selectMonth(month: string) {
    this.month = month;
    this.toggleCalendar();

    const monthIndex = this.months.indexOf(month);
    this.findByPlanning(monthIndex + 1, this.year);

  }

  message() {
    this.messageOperationService.message("Selecione o mês!", 'alert');
  }

  private findPlanningByDateCurrent() {
    const currentDate = new Date();
    const currentMonth = currentDate.getMonth() + 1;
    const currentYear = currentDate.getFullYear();

    //Adicionar o mes e ano atual no calendario
    this.month = this.months[currentDate.getMonth()];
    this.year = currentDate.getFullYear();

    this.findByPlanning(currentMonth, currentYear);
  }

  private findByPlanning(month: number, year: number) {
    this.financeiroService.findByDate(month, year).subscribe(response => {
      if (response) {
        this.planning = response;

        if (response.categoriesRecords) {
          this.categories = response.categoriesRecords as Categories[];
          this.dataSource = new MatTableDataSource<Categories>(this.categories);
          this.dataSource.paginator = this.paginator;

          this.showTable = true;
        }
      } else {
        this.categories = [];
        this.dataSource = new MatTableDataSource<Categories>(this.categories);
        this.dataSource.paginator = this.paginator;

        this.showTable = false;
      }
    }, error => {
      let errorMessage = 'Ocorreu um erro na operação. Por favor, tente novamente mais tarde.';

      switch (error.status) {
        case 401: {
          errorMessage = 'Credenciais inválidas';
        } break;
        case 403: {
          errorMessage = 'Acesso negado. Você não tem permissão para acessar este recurso.';
        } break;

      }

      this.messageOperationService.message(errorMessage, 'error');
    });
  }

}
