import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarrerasComponent } from './carreras.component';

describe('CarrerasComponent', () => {
  let component: CarrerasComponent;
  let fixture: ComponentFixture<CarrerasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarrerasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarrerasComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
