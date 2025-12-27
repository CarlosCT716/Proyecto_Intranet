import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SesionInicioComponent } from './sesion-inicio.component';

describe('SesionInicioComponent', () => {
  let component: SesionInicioComponent;
  let fixture: ComponentFixture<SesionInicioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SesionInicioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SesionInicioComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
