import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarreraFormComponent } from './carrera-form.component';

describe('CarreraFormComponent', () => {
  let component: CarreraFormComponent;
  let fixture: ComponentFixture<CarreraFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarreraFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarreraFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
