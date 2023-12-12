import { Component } from '@angular/core';
import { CategoryDefaultService } from './category-default.service';
import { CategoryDefault } from './category-default';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { CrudCategoryDefaultComponent } from './crud-category-default/crud-category-default.component';

@Component({
  selector: 'app-category-default',
  templateUrl: './category-default.component.html',
  styleUrls: ['./category-default.component.scss']
})
export class CategoryDefaultComponent {

  dataSource: any;
  displayedColumns: string[] = ['category', 'type', 'action'];

  categoryDefault: CategoryDefault[] = [];

  constructor(private categoryDefaultService: CategoryDefaultService,
              public dialog: MatDialog) { }


  ngOnInit() {
    this.findAll();
  }

  findAll() {
    this.categoryDefaultService.findAll().subscribe((response) => {
      this.categoryDefault = response as CategoryDefault[];
      this.dataSource = new MatTableDataSource<CategoryDefault>(this.categoryDefault);
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

      this.categoryDefaultService.message(errorMessage);
    })
  }

  createCategory(element: CategoryDefault | null){
    const dialogRef = this.dialog.open(CrudCategoryDefaultComponent, {
      width: '550px',
      maxHeight: '50vh',
      data: element === null ? {
        name: ''
      } : element });

      dialogRef.afterClosed().subscribe(response => {
        if (response !== undefined) {
          this.saveCategory(response);
        }
      });

  }

  private saveCategory(response: object) {
    this.categoryDefaultService.sendNewCategory(response).subscribe(response => {
      const currentData = this.dataSource.data;
      const newData = [...currentData, response];
      this.dataSource.data = newData;

      this.categoryDefaultService.message('Operação efetuada com sucesso!');
    }, error => {
      let errorMessage = 'Ocorreu um erro na operação. Por favor, tente novamente mais tarde.';

      switch (error.status) {
        case 400: {
          errorMessage = 'Dados inválidas. Certifique-se de que você forneceu dados válidos e tente novamente';
        } break;
        case 401: {
          errorMessage = 'Credenciais inválidas';
        } break;
        case 403: {
          errorMessage = 'Acesso negado. Você não tem permissão para acessar este recurso.';
        } break;
      }

      this.categoryDefaultService.message(errorMessage);
    });
  }
}
