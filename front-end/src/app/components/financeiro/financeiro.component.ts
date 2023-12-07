import { Component, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { FinanceiroService } from '../service/financeiro.service';
import { Planning } from 'src/app/shared/model/planning';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Categories } from 'src/app/shared/model/categories';

@Component({
  selector: 'app-financeiro',
  templateUrl: './financeiro.component.html',
  styleUrls: ['./financeiro.component.scss'],
})
export class FinanceiroComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatTable) table!: MatTable<any>;
  displayedColumns: string[] = ['category', 'planned', 'pagos', 'status', 'descricao', 'acao'];
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

  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });
  isLinear = false;

  planning!: Planning;
  categories: Categories[] = [];


  constructor(private _formBuilder: FormBuilder,
    private financeiroService: FinanceiroService) { }

  ngOnInit() {
    this.findPlanningByDateCurrent();
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
    this.financeiroService.message("Selecione o mês!");
  }

  private findPlanningByDateCurrent() {
    const currentDate = new Date();
    const currentMonth = currentDate.getMonth() + 1;
    const currentYear = currentDate.getFullYear();

    //Adicionar o mes e ano atual no customCalendar
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
    });
  }

}
