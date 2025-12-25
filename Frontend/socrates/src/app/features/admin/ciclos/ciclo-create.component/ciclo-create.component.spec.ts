import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CicloCreateComponent } from './ciclo-create.component';

describe('CicloCreateComponent', () => {
  let component: CicloCreateComponent;
  let fixture: ComponentFixture<CicloCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CicloCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CicloCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
