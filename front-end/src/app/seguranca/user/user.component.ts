import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from './user';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { UserService } from './user.service';
import { MatDialog } from '@angular/material/dialog';
import { UserCrudComponent } from './user-crud/user-crud.component';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  users: User[] = [];

  displayedColumns: string[] = ['name', 'status', 'username', 'roleList', 'acao'];
  dataSource: any;
  clickedRows = new Set<User>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatTable) table!: MatTable<any>;

  constructor(private userService: UserService,
              public dialog: MatDialog,
              private message: MessageOperationService) { }

  ngOnInit(): void {
    this.findAll();
  }

  findAll(): void {
    this.userService.findAll().subscribe((resposta) => {
      if ('content' in resposta) {
        this.users = resposta.content as User[];
        this.dataSource = new MatTableDataSource<User>(this.users);
        this.dataSource.paginator = this.paginator;
      }
    }, error => {
      switch (error.status) {
        case 409: {
          this.message.message('O endereço de e-mail já está em uso. Não é possível criar uma nova conta com este e-mail.', 'warn');
        } break;
      }
    })
  }

  createUser(element: User | null): void {
    const dialogRef = this.dialog.open(UserCrudComponent, {
      width: '550px',
      data: element === null ? {
        name: ''
      } : element
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        result.password = "12345678";
        this.saveNewUser(result);
      }
    });
  }

  private saveNewUser(result: object) {
    this.userService.sendNewUser(result).subscribe(response => {
      const currentData = this.dataSource.data;
      const newData = [...currentData, response];
      this.dataSource.data = newData;

      this.message.message('Operação efetuada com sucesso!', 'success');

    }, error => {
      switch (error.status) {
        case 409: {
          this.message.message('O endereço de e-mail já está em uso. Não é possível criar uma nova conta com este e-mail.', 'warn');
        } break;
      }
    })
  }

}
