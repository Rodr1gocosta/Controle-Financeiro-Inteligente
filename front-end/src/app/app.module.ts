import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { SharedModule } from './shared/shared.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { NgxCurrencyDirective } from 'ngx-currency';
import { NgxSpinnerModule } from 'ngx-spinner';

import {MatInputModule} from '@angular/material/input';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatButtonModule} from '@angular/material/button';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatDialogModule} from '@angular/material/dialog';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatMenuModule} from '@angular/material/menu';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatStepperModule} from '@angular/material/stepper';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatRadioModule} from '@angular/material/radio';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatPasswordStrengthModule} from '@angular-material-extensions/password-strength';

import { AppComponent } from './app.component';
import { LoginComponent } from './seguranca/login/login.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './template/header/header.component';
import { FooterComponent } from './template/footer/footer.component';
import { SidenavComponent } from './template/sidenav/sidenav.component';
import { SublevelMenuComponent } from './template/sidenav/sublevel-menu.component';
import { BodyComponent } from './template/body/body.component';

import { RealPipe } from './shared/pipe/RealPipe';

import { AuthInterceptor } from './seguranca/interceptor/auth.interceptor';
import { ErrorHandlerInterceptor } from './seguranca/interceptor/error-handler.interceptor';
import { SpinnerInterceptor } from './seguranca/interceptor/spinner.interceptor';

import { FinanceiroComponent } from './components/financeiro/financeiro.component';
import { InvestimentoComponent } from './components/investimento/investimento.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { FinanceiroCrudComponent } from './components/financeiro/financeiro-crud/financeiro-crud.component';
import { CategoryDefaultComponent } from './seguranca/admin/category-default/category-default.component';
import { CrudCategoryDefaultComponent } from './seguranca/admin/category-default/crud-category-default/crud-category-default.component';
import { CategoriaCrudComponent } from './components/financeiro/categoria-crud/categoria-crud.component';
import { PasswordResetComponent } from './seguranca/password/password-reset/password-reset.component';

@NgModule({
  declarations: [
    AppComponent,
    BodyComponent,
    LoginComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    SidenavComponent,
    SublevelMenuComponent,
    FinanceiroComponent,
    FinanceiroCrudComponent,
    InvestimentoComponent,
    DashboardComponent,
    RealPipe,
    CategoryDefaultComponent,
    CrudCategoryDefaultComponent,
    CategoriaCrudComponent,
    PasswordResetComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,

    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,

    NgxMaskDirective, 
    NgxMaskPipe,
    NgxCurrencyDirective,
    NgxSpinnerModule,

    MatPasswordStrengthModule.forRoot(),

    SharedModule,

    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatAutocompleteModule,
    MatInputModule,
    MatDatepickerModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDialogModule,
    MatTooltipModule,
    MatMenuModule,
    MatProgressBarModule,
    MatCheckboxModule,
    MatStepperModule,
    MatGridListModule,
    MatRadioModule,
    MatExpansionModule,
    MatProgressSpinnerModule
  ],
  providers: [ provideNgxMask(),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: SpinnerInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
