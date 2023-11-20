import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from './user';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { UserService } from './user.service';
import { MatDialog } from '@angular/material/dialog';
import { UserCrudComponent } from './user-crud/user-crud.component';

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
    public dialog: MatDialog) { }

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

      // Recupere os dados atuais do dataSource
      const currentData = this.dataSource.data;

      // Adicione os novos dados aos dados atuais
      const newData = [...currentData, response];

      // Atribua os novos dados ao dataSource.data
      this.dataSource.data = newData;

      this.userService.message('Operação efetuada com sucesso!');

    }, error => {
      let errorMessage = 'Ocorreu um erro na operação. Por favor, tente novamente mais tarde.';

      switch (error.status) {
        case 401: {
          errorMessage = 'Credenciais inválidas';
        } break;
        case 403: {
          errorMessage = 'Acesso negado. Você não tem permissão para acessar este recurso.';
        } break;
        case 409: {
          errorMessage = 'O endereço de e-mail já está em uso. Não é possível criar uma nova conta com este e-mail.';
        } break;
      }

      this.userService.message(errorMessage);
    })
  }

}
