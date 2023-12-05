import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { FinanceiroService } from '../service/financeiro.service';

// tabela teste
export interface PeriodicElement {
  name: number;
  position: string;
  weight: string;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {position: "Casa", name: 1100, weight: "Pago", symbol: 'H'},
  {position: "Alimentação", name: 500, weight: "Pago", symbol: 'He'},
  {position: "Carro", name: 400, weight: "Pendende", symbol: 'Li'},
  {position: "Comemoração", name: 700, weight: "Pendende", symbol: 'Be'},
  {position: "Cachorro", name: 300, weight: "Pago", symbol: 'B'},
  {position: "Faculdade", name: 800, weight: "Pendende", symbol: 'C'},
  {position: "Investimento", name: 1000, weight: "Pago", symbol: 'N'},
];
// tabela teste

@Component({
  selector: 'app-financeiro',
  templateUrl: './financeiro.component.html',
  styleUrls: ['./financeiro.component.scss'],
})
export class FinanceiroComponent {

  calendarioCustomButton!: string;
  currentMonth!: string;
  currentYear!: number;
  isCalendarOpen = false;
  months = [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ];

  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });
  isLinear = false;

// tabela teste
  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  dataSource = ELEMENT_DATA;
// tabela teste


  constructor(private _formBuilder: FormBuilder,
              private financeiroService: FinanceiroService) { }

  ngOnInit() {
    this.calendarioCustom();
  }

  decreasesYear() {
    if (this.isCalendarOpen) {
      this.currentYear = this.currentYear - 1;
    } else {
      const currentMonthIndex = this.months.indexOf(this.currentMonth);
      const previousMonthIndex = (currentMonthIndex - 1 + 12) % 12;

      this.currentMonth = this.months[previousMonthIndex];

      if (this.currentMonth === 'Dezembro') {
        this.currentYear = this.currentYear - 1;
      }
      console.log('Mês selecionado:', this.currentMonth, '| Ano selecionado:', this.currentYear);
    }
  }

  incrementYear() {
    if (this.isCalendarOpen) {
      this.currentYear = this.currentYear + 1;
    } else {
      const currentMonthIndex = this.months.indexOf(this.currentMonth);
      const previousMonthIndex = (currentMonthIndex + 1 + 12) % 12;

      this.currentMonth = this.months[previousMonthIndex];

      if (this.currentMonth === 'Janeiro') {
        this.currentYear = this.currentYear + 1;
      } 
      console.log('Mês selecionado:', this.currentMonth, '| Ano selecionado:', this.currentYear);
    }
  }

  toggleCalendar() {
    this.isCalendarOpen = !this.isCalendarOpen;
  }

  selectMonth(month: string) {
    this.currentMonth = month;
    this.toggleCalendar();
    console.log('Mês selecionado:', month, '| Ano selecionado:', this.currentYear);

  }

  message() {
    this.financeiroService.message("Selecione o mês!");
  }

  private calendarioCustom() {
    const currentDate = new Date();
    this.currentMonth = this.months[currentDate.getMonth()];
    this.currentYear = currentDate.getFullYear();
  }
}
