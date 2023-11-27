import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvestimentoComponent } from './investimento.component';

describe('InvestimentoComponent', () => {
  let component: InvestimentoComponent;
  let fixture: ComponentFixture<InvestimentoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvestimentoComponent]
    });
    fixture = TestBed.createComponent(InvestimentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
