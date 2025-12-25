import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HorarioCreateComponent } from './horario-create.component';

describe('HorarioCreateComponent', () => {
  let component: HorarioCreateComponent;
  let fixture: ComponentFixture<HorarioCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HorarioCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HorarioCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
