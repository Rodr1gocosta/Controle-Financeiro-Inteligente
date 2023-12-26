import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryDefault } from 'src/app/seguranca/admin/category-default/category-default';
import { CategoryDefaultService } from 'src/app/seguranca/admin/category-default/category-default.service';
import { Planning } from 'src/app/shared/model/planning';
import { CategoriaCrudService } from '../../service/categoria-crud.service';
import { ConfirmationDialogService } from 'src/app/shared/util/confirmation-dialog/confirmation-dialog.service';
import { Categories } from 'src/app/shared/model/categories';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

@Component({
  selector: 'app-categoria-crud',
  templateUrl: './categoria-crud.component.html',
  styleUrls: ['./categoria-crud.component.scss']
})
export class CategoriaCrudComponent {
  dataSource: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['category', 'type', 'descricao', 'planned', 'action'];

  categorysEssenciais: CategoryDefault[] = [];
  categorysNaoEssenciais: CategoryDefault[] = [];
  categories: Categories[] = [];
  planning: Planning = {};

  tipoDespesa!: number;
  element: any;

  modoEdicao = false;

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
    public dialogRef: MatDialogRef<CategoriaCrudComponent>,
    private formBuilder: FormBuilder,
    private categoryDefaultService: CategoryDefaultService,
    private categoriaCrudService: CategoriaCrudService,
    private confirmationDialogService: ConfirmationDialogService,
    private messageOperationService: MessageOperationService) { }

  ngOnInit() {
    this.findAllByCategorys();
  }

  onTipoDespesaChange(event: any): void {
    this.tipoDespesa = event.value;
  }

  findAllByCategorys() {
    this.categoryDefaultService.findAll().subscribe(response => {
      this.categorysEssenciais = response.filter(categorys => categorys.typeCategory == 'ESSENCIAL');
      this.categorysNaoEssenciais = response.filter(categorys => categorys.typeCategory == 'NAO_ESSENCIAL');
    });
  }

  saveListFromCategorys() {
    this.confirmationDialogService
      .openConfirmationDialog('Deseja salvar essa operação?')
      .subscribe(response => {

        if (response) {
          this.dataSource.data.forEach(object => this.categories.push(object));

          if (this.data) {
            this.planning.id = this.data.id;
            if(this.planning.id) {
              this.categoriaCrudService.sendNewListCategorias(this.categories, this.planning.id).subscribe(response => {

                this.dialogRef.close(response);
                this.messageOperationService.message('Operação realizado com sucesso!', 'success')

              }, error => {
                this.categories = [];
              });
            }
          }
        }
      });
  }

  adicionarDespesas() {
    if (this.modoEdicao) {

      const index = this.dataSource.data.indexOf(this.element);
      if (index >= 0) {
        this.dataSource.data[index] = this.categoriesForm.value;
        this.dataSource._updateChangeSubscription();
      }

      this.modoEdicao = false;
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

      this.dataSource.data.push(this.categoriesForm.value);
      this.dataSource._updateChangeSubscription();
    }

    this.categoriesForm.reset();
  }

  editar(element: any) {
    // Ative o modo de edição
    this.modoEdicao = true;
    this.element = element;

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

  deletar(element: any) {
    const index = this.dataSource.data.indexOf(element);

    if (index >= 0) {
      this.dataSource.data.splice(index, 1);
      this.dataSource._updateChangeSubscription();
    }
  }

  clear() {
    this.categoriesForm.reset();
  }

  onCancel() {
    this.dialogRef.close();
  }

  getTotalDespesas() {
    if (this.dataSource && this.dataSource.data) {
      return this.dataSource.data.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }

  getTotalDespesasEssenciais() {
    if (this.dataSource && this.dataSource.data) {
      const despesasEssenciais = this.dataSource.data.filter(item =>
        item.category && item.category.typeCategory === 'ESSENCIAL'
      );

      return despesasEssenciais.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }

  getTotalDespesasNaoEssenciais() {
    if (this.dataSource && this.dataSource.data) {
      const despesasNaoEssenciais = this.dataSource.data.filter(item =>
        item.category && item.category.typeCategory === 'NAO_ESSENCIAL'
      );

      return despesasNaoEssenciais.map(item => item.planned || 0).reduce((acc, value) => acc + value, 0);
    }
    return 0;
  }

}
