import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AulasCreateComponent } from './aulas-create.component';

describe('AulasCreateComponent', () => {
  let component: AulasCreateComponent;
  let fixture: ComponentFixture<AulasCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AulasCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AulasCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
