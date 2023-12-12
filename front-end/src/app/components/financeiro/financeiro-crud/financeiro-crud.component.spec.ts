import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinanceiroCrudComponent } from './financeiro-crud.component';

describe('FinanceiroCrudComponent', () => {
  let component: FinanceiroCrudComponent;
  let fixture: ComponentFixture<FinanceiroCrudComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinanceiroCrudComponent]
    });
    fixture = TestBed.createComponent(FinanceiroCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
