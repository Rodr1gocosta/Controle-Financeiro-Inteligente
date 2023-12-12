import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudCategoryDefaultComponent } from './crud-category-default.component';

describe('CrudCategoryDefaultComponent', () => {
  let component: CrudCategoryDefaultComponent;
  let fixture: ComponentFixture<CrudCategoryDefaultComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrudCategoryDefaultComponent]
    });
    fixture = TestBed.createComponent(CrudCategoryDefaultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
