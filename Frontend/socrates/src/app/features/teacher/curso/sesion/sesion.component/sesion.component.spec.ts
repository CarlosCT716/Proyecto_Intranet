import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SesionComponent } from './sesion.component';

describe('SesionComponent', () => {
  let component: SesionComponent;
  let fixture: ComponentFixture<SesionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SesionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SesionComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
