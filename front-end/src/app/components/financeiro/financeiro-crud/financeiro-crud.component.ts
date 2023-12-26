import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryDefault } from 'src/app/seguranca/admin/category-default/category-default';
import { CategoryDefaultService } from 'src/app/seguranca/admin/category-default/category-default.service';
import { Categories } from 'src/app/shared/model/categories';
import { Planning } from 'src/app/shared/model/planning';
import { FinanceiroService } from '../../service/financeiro.service';
import { ConfirmationDialogService } from 'src/app/shared/util/confirmation-dialog/confirmation-dialog.service';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

@Component({
  selector: 'app-financeiro-crud',
  templateUrl: './financeiro-crud.component.html',
  styleUrls: ['./financeiro-crud.component.scss']
})
export class FinanceiroCrudComponent {
  dataSourceEtapa2: MatTableDataSource<any> = new MatTableDataSource<any>([]);

  displayedColumnsResumo: string[] = ['categoria', 'tipo', 'descricao', 'valor'];
  displayedColumns: string[] = ['category', 'type', 'descricao', 'planned', 'action'];

  categorysEssenciais: CategoryDefault[] = [];
  categorysNaoEssenciais: CategoryDefault[] = [];

  planning: Planning = {};
  categories: Categories[] = [];

  elementEtapa2: any;

  calculationOfValues: number = 0;
  planningTotalPlanned: number = 0;
  tipoDespesa!: number;

  modoEdicaoEtapa2 = false;
  panelOpenState = false;
  isLinear = true;

  planningForm = this.formBuilder.group({
    totalPlanned: ['', [Validators.required]],
  });

  categoriesForm = this.formBuilder.group({
    category: this.formBuilder.group({
      id: [''],
      name: [''],
      typeCategory: ['']
    }),
    descricao: [null],
    planned: ['', Validators.required],
  });

  constructor(@Inject(MAT_DIALOG_DATA)
    public data: Planning,
    public dialogRef: MatDialogRef<FinanceiroCrudComponent>,
    private formBuilder: FormBuilder,
    private categoryDefaultService: CategoryDefaultService,
    private financeiroService: FinanceiroService,
    private confirmationDialogService: ConfirmationDialogService,
    private messageOperationService: MessageOperationService
  ) { }

  ngOnInit() {
    this.findAllByCategorys();
    this.treatsValueTotalPlanned();
    this.findValueTotalPlannend();
  }

  onTipoDespesaChange(event: any): void {
    this.tipoDespesa = event.value;
  }

  findValueTotalPlannend() {
    const totalPlanned = this.planningForm.get('totalPlanned');

    if (totalPlanned) {
      totalPlanned.valueChanges.subscribe((value) => {
        this.planningTotalPlanned = Number(value) || 0;
      });
    }
  }

  treatsValueTotalPlanned() {
    const totalPlannedControl = this.planningForm.get('totalPlanned');

    if (totalPlannedControl) {
      totalPlannedControl.valueChanges.subscribe((newValue) => {
        if (newValue !== null && this.dataSourceEtapa2.data.length == 0) {
          this.calculationOfValues = parseFloat(newValue) || 0;
        } else if (newValue !== null && this.dataSourceEtapa2.data.length > 0) {
          this.calculationOfValues = parseFloat(newValue) - this.getTotalDespesas();
        }
      });
    }
  }

  adicionarDespesas() {
    const valorDespesaControl = this.categoriesForm.get('planned');

    if (valorDespesaControl && valorDespesaControl.value !== null) {
      const valorDespesa: number = parseFloat(valorDespesaControl.value);

      if (!isNaN(valorDespesa)) {
        if (this.modoEdicaoEtapa2) {

          const index = this.dataSourceEtapa2.data.indexOf(this.elementEtapa2);
          if (index >= 0) {
            this.dataSourceEtapa2.data[index] = this.categoriesForm.value;
            this.calculationOfValues = this.calculationOfValues + this.elementEtapa2.planned;
            this.updateCalculationOfValue(valorDespesa);
            this.dataSourceEtapa2._updateChangeSubscription();
          }

          this.modoEdicaoEtapa2 = false;
        } else {

          const categoryControl = this.categoriesForm.get('category');

          if (categoryControl && categoryControl.value) {
            const categoryName = categoryControl.value.name;
            const foundCategory = this.categorysEssenciais.find(category => category.name === categoryName) || this.categorysNaoEssenciais.find(category => category.name === categoryName);

            if (foundCategory) {
              this.categoriesForm.patchValue({
                category: {
                  ...this.categoriesForm.value.category,
                  typeCategory: foundCategory.typeCategory,
                  id: foundCategory.id
                }
              });
            }
          }

          this.dataSourceEtapa2.data.push(this.categoriesForm.value);
          this.updateCalculationOfValue(valorDespesa);
          this.dataSourceEtapa2._updateChangeSubscription();

        }
        this.categoriesForm.reset();
      } else {
        console.error('O valor da despesa não é um número válido.');
      }
    } else {
      console.error('O valor da despesa é nulo ou indefinido.');
    }
  }

  private updateCalculationOfValue(valorAtualizado: number) {
    this.calculationOfValues = this.calculationOfValues - valorAtualizado;
  }

  editarEtapa2(element: any) {
    // Ative o modo de edição
    this.modoEdicaoEtapa2 = true;
    this.elementEtapa2 = element;

    if (element.category) {

      if (element.category.typeCategory === 'ESSENCIAL') {
        this.tipoDespesa = 1;
      } else {
        this.tipoDespesa = 2;
      }

      // Se 'category' existe, trata como um objeto categoriesForm completo
      this.categoriesForm.setValue({
        category: {
          id: element.category.id,
          name: element.category.name,
          typeCategory: element.category.typeCategory
        },
        descricao: element.descricao,
        planned: element.planned,
      });
    } else {
      // Preencha o formulário com os dados do item selecionado
      this.categoriesForm.setValue({
        descricao: element.descricao,
        planned: element.planned
      } as any);
    }
  }

  deletarEtapa2(element: any) {
    const index = this.dataSourceEtapa2.data.indexOf(element);

    if (index >= 0) {
      this.dataSourceEtapa2.data.splice(index, 1);
      this.calculationOfValues = this.calculationOfValues + element.planned;
      this.dataSourceEtapa2._updateChangeSubscription();
    }
  }

  findAllByCategorys() {
    this.categoryDefaultService.findAll().subscribe(response => {
      this.categorysEssenciais = response.filter(categorys => categorys.typeCategory == 'ESSENCIAL');
      this.categorysNaoEssenciais = response.filter(categorys => categorys.typeCategory == 'NAO_ESSENCIAL');
    });
  }

  hasDataInTableEtapa2(): boolean {
    return this.dataSourceEtapa2 && this.dataSourceEtapa2.data && this.dataSourceEtapa2.data.length > 0;
  }

  clear() {
    this.categoriesForm.reset();
  }

  onCancel() {
    this.dialogRef.close();
  }

  save() {
    this.confirmationDialogService
      .openConfirmationDialog('Deseja salvar essa operação?')
      .subscribe(response => {
        if (response) {
          this.dataSourceEtapa2.data.forEach(object => this.categories.push(object));

          if (this.data) {
            this.planning.month = this.data.month;
            this.planning.year = this.data.year;
            this.planning.totalPlanned = this.planningTotalPlanned;
            this.planning.categoriesRecords = this.categories;
          }

          this.financeiroService.sendNewPlanning(this.planning).subscribe(response => {

            if (response) {
              this.dialogRef.close(response);
              this.messageOperationService.message('Operação realizado com sucesso!', 'success')
            }
          }, error => {
            this.categories = [];
          });
        }
      })
  }

  getTotalDespesas() {
    if (this.dataSourceEtapa2 && this.dataSourceEtapa2.data) {
      return this.dataSourceEtapa2.data.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }

  getTotalDespesasEssenciais() {
    if (this.dataSourceEtapa2 && this.dataSourceEtapa2.data) {
      const despesasEssenciais = this.dataSourceEtapa2.data.filter(item =>
        item.category && item.category.typeCategory === 'ESSENCIAL'
      );

      return despesasEssenciais.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }

  getTotalDespesasNaoEssenciais() {
    if (this.dataSourceEtapa2 && this.dataSourceEtapa2.data) {
      const despesasNaoEssenciais = this.dataSourceEtapa2.data.filter(item =>
        item.category && item.category.typeCategory === 'NAO_ESSENCIAL'
      );

      return despesasNaoEssenciais.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }
}
