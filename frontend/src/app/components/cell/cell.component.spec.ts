import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CellComponent } from './cell.component';

describe('CellComponent', () => {
  let component: CellComponent;
  let fixture: ComponentFixture<CellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CellComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(CellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create Cell component with default values', () => {
    expect(component.value).toBeUndefined();
    expect(component.isYellow).toBeFalsy();
    expect(component.isGreen).toBeFalse();
  });

  it('should be able to set value property of cell component', () => {
    component.value = 42;
    component.isYellow = true;
    component.isGreen = true;

    expect(component.value).toBe(42);
    expect(component.isGreen).toBeTrue();
    expect(component.isYellow).toBeTrue();
  });
});
