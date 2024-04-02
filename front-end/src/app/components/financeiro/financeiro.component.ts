import { Component, ViewChild } from '@angular/core';
import { FinanceiroService } from '../service/financeiro.service';
import { Planning } from 'src/app/shared/model/planning';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Categories } from 'src/app/shared/model/categories';
import { MatDialog } from '@angular/material/dialog';
import { FinanceiroCrudComponent } from './financeiro-crud/financeiro-crud.component';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';
import { SelectionModel } from '@angular/cdk/collections';
import { ConfirmationDialogService } from 'src/app/shared/util/confirmation-dialog/confirmation-dialog.service';
import { Subscription, throwError } from 'rxjs';
import { CategoriaCrudComponent } from './categoria-crud/categoria-crud.component';

export interface DisplayColumn {
  def: string;
  label: string;
  hide: boolean;
}

@Component({
  selector: 'app-financeiro',
  templateUrl: './financeiro.component.html',
  styleUrls: ['./financeiro.component.scss']
})
export class FinanceiroComponent {
  @ViewChild(MatTable) table!: MatTable<any>;
  ELEMENT_DATA!: Categories[];
  dataSource = new MatTableDataSource<Categories>(this.ELEMENT_DATA);
  showTable: boolean = true;
  selection!: SelectionModel<Categories>;
  displayedColumns: DisplayColumn[] = [
    { def: 'select', label: 'Selecione', hide: false },
    { def: 'category', label: 'Categoria', hide: false },
    { def: 'tipo', label: 'Tipo', hide: false },
    { def: 'planned', label: 'Valor', hide: false },
    { def: 'status', label: 'Status', hide: false },
    { def: 'descricao', label: 'Descrição', hide: false },
    { def: 'acao', label: 'Ação', hide: false }
  ];

  disColumns!: string[];
  checkBoxList: DisplayColumn[] = [];

  calendarioCustomButton!: string;
  month!: string;
  year!: number;
  isCalendarOpen = false;
  months = [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ];

  selectedTypeCategory: string = 'all';
  value: string = '';

  planningId!: string;

  categories: Categories[] = [];
  salario: number = 0;
  gastos: number = 0;
  rendimento: number = 0;

  private tabelaAtualizadaSubscription: Subscription | undefined;

  constructor(private financeiroService: FinanceiroService,
    public dialog: MatDialog,
    private messageOperationService: MessageOperationService,
    private confirmationDialogService: ConfirmationDialogService) { }

  ngOnInit() {
    this.selection = new SelectionModel<Categories>(true, []);
    this.disColumns = this.displayedColumns.map(cd => cd.def)

    this.findPlanningByDateCurrent();
  }

  ngOnDestroy(): void {
    if (this.tabelaAtualizadaSubscription) {
      this.tabelaAtualizadaSubscription.unsubscribe();
    }
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

    dialogRef.afterClosed().subscribe(updatedData => {
      if (updatedData) {
        this.dataSource.data = updatedData.categoriesRecords;
        this.salario = updatedData.totalPlanned;
        this.gastos = this.getTotalDespesas();
        this.rendimento = this.salario - this.gastos;
        this.showTable = true;
      }
    });
  }

  createCategory() {
    const dialogRef = this.dialog.open(CategoriaCrudComponent, {
      width: '100%',
      maxHeight: '78vh',
      data: {
        id: this.planningId
      }
    });

    dialogRef.afterClosed().subscribe(updatedData => {
      if (updatedData) {
        this.dataSource.data = updatedData.categoriesRecords;
        this.gastos = this.getTotalDespesas();
        this.rendimento = this.salario - this.gastos;
      }
    });
  }

  delete() {
    this.financeiroService.deletePlanning(this.planningId).subscribe(() => {
      this.messageOperationService.message('Operação realizado com sucesso!', 'success');
    })
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
        if (response.id) {
          this.planningId = response.id;
        }

        if (response.categoriesRecords) {
          this.categories = response.categoriesRecords as Categories[];
          this.dataSource = new MatTableDataSource<Categories>(this.categories);

          this.showTable = true;
          this.selectedTypeCategory = 'all';
        }

        if (response.totalPlanned) {
          this.salario = response.totalPlanned;
          this.gastos = this.getTotalDespesas();
          this.rendimento = this.salario - this.gastos;
        }
      } else {
        this.categories = [];
        this.dataSource = new MatTableDataSource<Categories>(this.categories);

        this.salario = 0;
        this.gastos = 0;
        this.rendimento = 0;
        this.showTable = false;
      }
    });
  }

  public onSelectTypeCategory(): void {
    if (this.selectedTypeCategory === 'ESSENCIAL' || this.selectedTypeCategory === 'NAO_ESSENCIAL') {
      this.dataSource.filterPredicate = (data, filter) => {
        return data.category.typeCategory === filter;
      }
      this.dataSource.filter = this.selectedTypeCategory;
    } else {
      this.dataSource.filter = '';
    }
  }

  applyFilter(event: any): void {
    const filterValue = (event.target.value || '').toString().trim().toLowerCase();

    this.dataSource.filterPredicate = (data, filter) => {
      const dataString = `${data.category.name} ${data.planned} ${data.descricao}`.toLowerCase();
      return dataString.includes(filter);
    }

    this.dataSource.filter = filterValue;
  }

  isAllSelected(): boolean {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  masterToggle(): void {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.data.forEach(row => this.selection.select(row));
  }

  showCheckBoxes(): void {
    this.checkBoxList = this.displayedColumns;
  }

  hideCheckBoxes(): void {
    this.checkBoxList = [];
  }

  toggleForm(): void {
    this.checkBoxList.length ? this.hideCheckBoxes() : this.showCheckBoxes();
  }

  hideColumn(event: any, item: string) {
    this.displayedColumns.forEach(element => {
      if (element['def'] == item) {
        element['hide'] = event.checked;
      }
    });
    this.disColumns = this.displayedColumns.filter(cd => !cd.hide).map(cd => cd.def)
  }

  openDeleteDialog(len: number, obj: Categories[]): void {
    let elemento = 'elemento';
    if (len > 1) {
      elemento = 'elementos';
    }

    let idsList: string[] = [];
    if (Array.isArray(obj)) {
      idsList = obj.map(category => category.id);
    } else {
      let categories: Categories = obj;
      idsList.push(categories.id);
    }

    this.confirmationDialogService
      .openConfirmationDialog(`Tem certeza de que deseja remover ${len} ${elemento}?`)
      .subscribe(response => {
        if (response) {
          this.deleteCategories(idsList);
        }
      });
  }

  private deleteCategories(idsList: string[]): void {
    this.financeiroService.deleteCategories(idsList, this.planningId).subscribe({
      next: response => {
        this.dataSource.data = this.dataSource.data.filter(item => !idsList.includes(item.id));
        this.selection.clear();
        this.messageOperationService.message('Operação realizado com sucesso!', 'success');
      },
      error: error => {
        this.messageOperationService.message('Erro ao realizar a operação!', 'error');
      }
    });
  }

  getTotalDespesas() {
    if (this.dataSource && this.dataSource.data) {
      return this.dataSource.data.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }

  onDownloadPDF() {
    this.financeiroService.onDownloadPDF(this.planningId).subscribe((data: Blob) => {
      const file = new Blob([data], { type: data.type });
      const blob = window.URL.createObjectURL(file);
      const link = document.createElement('a');

      link.href = blob;

      link.download = `planejamento_${this.month}_${this.year}.pdf`;

      link.click();

      window.URL.revokeObjectURL(blob);
      link.remove();
    }, error => {
      if (error.status === 404) {
        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const jsonResponse = JSON.parse(reader.result as string);

            if (jsonResponse && jsonResponse.error) {
              const errorMessage = jsonResponse.error;
              this.messageOperationService.message(errorMessage, 'error');
            } else {
              console.error('Formato JSON inválido:', reader.result);
            }
          } catch (parseError) {
            console.error('Erro ao fazer o parsing do JSON:', parseError);
          }
        };

        reader.readAsText(error.error);
      } else {
        console.error('Erro desconhecido:', error);
      }

      return throwError(error);
    });
  }

}
