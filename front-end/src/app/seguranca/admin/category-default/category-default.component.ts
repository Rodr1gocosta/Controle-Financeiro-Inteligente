import { Component } from '@angular/core';
import { CategoryDefaultService } from './category-default.service';
import { CategoryDefault } from './category-default';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { CrudCategoryDefaultComponent } from './crud-category-default/crud-category-default.component';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

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
              public dialog: MatDialog,
              private message: MessageOperationService) { }


  ngOnInit() {
    this.findAll();
  }

  findAll() {
    this.categoryDefaultService.findAll().subscribe((response) => {
      this.categoryDefault = response as CategoryDefault[];
      this.dataSource = new MatTableDataSource<CategoryDefault>(this.categoryDefault);
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

      this.message.message('Operação efetuada com sucesso!', 'success');
    });
  }
}
