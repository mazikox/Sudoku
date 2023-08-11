import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPayeesComponent } from './add-payees.component';

describe('AddPayeesComponent', () => {
  let component: AddPayeesComponent;
  let fixture: ComponentFixture<AddPayeesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddPayeesComponent]
    });
    fixture = TestBed.createComponent(AddPayeesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
