import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoliBouton } from './joli-bouton';

describe('JoliBouton', () => {
  let component: JoliBouton;
  let fixture: ComponentFixture<JoliBouton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JoliBouton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JoliBouton);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
