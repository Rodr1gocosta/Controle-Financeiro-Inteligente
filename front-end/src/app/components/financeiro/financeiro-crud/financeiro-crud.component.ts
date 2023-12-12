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
  dataSourceEtapa3: MatTableDataSource<any> = new MatTableDataSource<any>([]);

  displayedColumns: string[] = ['category', 'descricao', 'planned', 'action'];

  categorysEssenciais: CategoryDefault[] = [];
  categorysNaoEssenciais: CategoryDefault[] = [];

  modoEdicaoEtapa2: boolean = false;
  modoEdicaoEtapa3: boolean = false;

  elementEtapa2: any;
  elementEtapa3: any;

  exibirInstrucoesEtapa2: boolean = false;
  exibirInstrucoesEtapa3: boolean = false;


  isLinear = true;

  planning = this.formBuilder.group({
    totalPlanned: ['', [Validators.required]],
  });

  despesasEssenciais = this.formBuilder.group({
    category: this.formBuilder.group({
      name: ['', Validators.required]
    }),
    descricao: [''],
    planned: ['', Validators.required]
  });

  despesasNaoEssenciais = this.formBuilder.group({
    category: this.formBuilder.group({
      name: ['', Validators.required]
    }),
    descricao: [''],
    planned: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              private categoryDefaultService: CategoryDefaultService) { }

  ngOnInit() {
    this.findAllByCategorys()
  }

  adicionarDespesasEssenciais() {
    if (this.modoEdicaoEtapa2) {
      const index = this.dataSourceEtapa2.data.indexOf(this.elementEtapa2);
      if (index >= 0) {
        this.dataSourceEtapa2.data[index] = this.despesasEssenciais.value;

        this.dataSourceEtapa2._updateChangeSubscription();
      }
      this.modoEdicaoEtapa2 = false;

    } else {
      this.dataSourceEtapa2.data.push(this.despesasEssenciais.value);
      this.dataSourceEtapa2._updateChangeSubscription();

    }
    this.despesasEssenciais.reset();    
  }

  adicionarDespesasNaoEssenciais() {
    if (this.modoEdicaoEtapa3) {
      const index = this.dataSourceEtapa3.data.indexOf(this.elementEtapa3);
      if (index >= 0) {
        this.dataSourceEtapa3.data[index] = this.despesasNaoEssenciais.value;

        this.dataSourceEtapa3._updateChangeSubscription();
      }
      this.modoEdicaoEtapa3 = false;

    } else {
      this.dataSourceEtapa3.data.push(this.despesasNaoEssenciais.value);
      this.dataSourceEtapa3._updateChangeSubscription();

    }
    this.despesasNaoEssenciais.reset();    
  }

  editarEtapa2(element: any) {
    // Ative o modo de edição
    this.modoEdicaoEtapa2 = true;
    this.elementEtapa2 = element;

    if (element.category) {
      // Se 'category' existe, trata como um objeto despesasEssenciais completo
      this.despesasEssenciais.setValue({
        category: { name: element.category.name },
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

  editarEtapa3(element: any) {
    // Ative o modo de edição
    this.modoEdicaoEtapa3 = true;
    this.elementEtapa3 = element;

    if (element.category) {
      // Se 'category' existe, trata como um objeto despesasNãoEssenciais completo
      this.despesasNaoEssenciais.setValue({
        category: { name: element.category.name },
        descricao: element.descricao,
        planned: element.planned,
      });
    } else {
      // Preencha o formulário com os dados do item selecionado
      this.despesasNaoEssenciais.setValue({
        descricao: element.descricao,
        planned: element.planned
      } as any);
    }
  }

  deletarEtapa2(element: any) {
    const index = this.dataSourceEtapa2.data.indexOf(element);
    if (index >= 0) {
      this.dataSourceEtapa2.data.splice(index, 1);

      this.dataSourceEtapa2._updateChangeSubscription();
    }
  }

  deletarEtapa3(element: any) {
    const index = this.dataSourceEtapa3.data.indexOf(element);
    if (index >= 0) {
      this.dataSourceEtapa3.data.splice(index, 1);

      this.dataSourceEtapa3._updateChangeSubscription();
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

  hasDataInTableEtapa3(): boolean {
    return this.dataSourceEtapa3 && this.dataSourceEtapa3.data && this.dataSourceEtapa3.data.length > 0;
  }

}
