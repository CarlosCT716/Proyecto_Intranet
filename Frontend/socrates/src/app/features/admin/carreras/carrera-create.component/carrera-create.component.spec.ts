import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarreraCreateComponent } from './carrera-create.component';

describe('CarreraCreateComponent', () => {
  let component: CarreraCreateComponent;
  let fixture: ComponentFixture<CarreraCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarreraCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarreraCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
