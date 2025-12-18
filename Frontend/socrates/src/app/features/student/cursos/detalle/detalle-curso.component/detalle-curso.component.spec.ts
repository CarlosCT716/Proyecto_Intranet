import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleCursoComponent } from './detalle-curso.component';

describe('DetalleCursoComponent', () => {
  let component: DetalleCursoComponent;
  let fixture: ComponentFixture<DetalleCursoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleCursoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleCursoComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
