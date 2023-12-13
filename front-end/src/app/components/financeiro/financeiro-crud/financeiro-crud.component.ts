import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryDefault } from 'src/app/seguranca/admin/category-default/category-default';
import { CategoryDefaultService } from 'src/app/seguranca/admin/category-default/category-default.service';

@Component({
  selector: 'app-financeiro-crud',
  templateUrl: './financeiro-crud.component.html',
  styleUrls: ['./financeiro-crud.component.scss']
})
export class FinanceiroCrudComponent {
  dataSourceEtapa2: MatTableDataSource<any> = new MatTableDataSource<any>([]);

  displayedColumnsResumo: string[] = ['categoria', 'tipo',  'descricao', 'valor'];
  displayedColumns: string[] = ['category', 'type', 'descricao', 'planned', 'action'];

  categorysEssenciais: CategoryDefault[] = [];
  categorysNaoEssenciais: CategoryDefault[] = [];

  elementEtapa2: any;

  calculationOfValues: number = 0;
  tipoDespesa!: number;

  modoEdicaoEtapa2: boolean = false;
  panelOpenState = false;
  isLinear = true;

  planning = this.formBuilder.group({
    totalPlanned: ['', [Validators.required]],
  });

  despesasEssenciais = this.formBuilder.group({
    category: this.formBuilder.group({
      name: ['', Validators.required],
      typeCategory: ['']
    }),
    descricao: [''],
    planned: ['', Validators.required],
  });

  constructor(private formBuilder: FormBuilder,
              private categoryDefaultService: CategoryDefaultService) { }

  ngOnInit() {
    this.findAllByCategorys();
    this.treatsValueTotalPlanned();
  }

  onTipoDespesaChange(event: any): void {
    this.tipoDespesa = event.value;
  }

  treatsValueTotalPlanned() {
    const totalPlannedControl = this.planning.get('totalPlanned');

    if (totalPlannedControl) {
      totalPlannedControl.valueChanges.subscribe((newValue) => {
        if (newValue !== null) {
          this.calculationOfValues = parseFloat(newValue) || 0;
        }
      });
    }
  }

  adicionarDespesasEssenciais() {
    const valorDespesaControl = this.despesasEssenciais.get('planned');

    if (valorDespesaControl && valorDespesaControl.value !== null) {
      const valorDespesa: number = parseFloat(valorDespesaControl.value);

      if (!isNaN(valorDespesa)) {
        if (this.modoEdicaoEtapa2) {

          const index = this.dataSourceEtapa2.data.indexOf(this.elementEtapa2);
          if (index >= 0) {
            this.dataSourceEtapa2.data[index] = this.despesasEssenciais.value;
            this.calculationOfValues = this.calculationOfValues + this.elementEtapa2.planned;
            this.updateCalculationOfValue(valorDespesa);
            this.dataSourceEtapa2._updateChangeSubscription();
          }

          this.modoEdicaoEtapa2 = false;
        } else {

          const categoryControl = this.despesasEssenciais.get('category');

          if (categoryControl && categoryControl.value) {
            const categoryName = categoryControl.value.name;
            const foundCategory = this.categorysEssenciais.find(category => category.name === categoryName) || this.categorysNaoEssenciais.find(category => category.name === categoryName);

            if (foundCategory) {
              this.despesasEssenciais.patchValue({
                category: {
                  ...this.despesasEssenciais.value.category,
                  typeCategory: foundCategory.typeCategory
                }
              });
            }
          }

          this.dataSourceEtapa2.data.push(this.despesasEssenciais.value);
          this.updateCalculationOfValue(valorDespesa);
          this.dataSourceEtapa2._updateChangeSubscription();

        }
        this.despesasEssenciais.reset();
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

      // Se 'category' existe, trata como um objeto despesasEssenciais completo
      this.despesasEssenciais.setValue({
        category: {
          name: element.category.name,
          typeCategory: element.category.typeCategory
        },
        descricao: element.descricao,
        planned: element.planned,
      });
    } else {
      // Preencha o formulário com os dados do item selecionado
      this.despesasEssenciais.setValue({
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
    this.despesasEssenciais.reset();
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
