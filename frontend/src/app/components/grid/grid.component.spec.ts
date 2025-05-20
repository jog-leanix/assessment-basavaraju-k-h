import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GridComponent } from './grid.component';
import { GridService } from '../../services/grid.service';
import { of } from 'rxjs';
import { Cell } from '../../models/Cell';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { mock, instance, when, verify } from 'ts-mockito';
import {environment} from '../../../environments/environment';

describe('GridComponent', () => {
  let component: GridComponent;
  let fixture: ComponentFixture<GridComponent>;
  let gridServiceMock: GridService;
  let gridServiceInstance: GridService;

  // Mock data
  const mockGrid: Cell[][] = [
    [{ row: 0, column: 0, value: 1 }, { row: 0, column: 1, value: 2 }],
    [{ row: 1, column: 0, value: 3 }, { row: 1, column: 1, value: 4 }]
  ];

  beforeEach(async () => {
    gridServiceMock = mock(GridService);
    gridServiceInstance = instance(gridServiceMock);

    await TestBed.configureTestingModule({
      imports: [GridComponent],
      providers: [
        { provide: GridService, useValue: gridServiceInstance }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Ignore unknown elements and attributes
    }).compileComponents();

    fixture = TestBed.createComponent(GridComponent);
    component = fixture.componentInstance;
  });

  it('should call createGrid and loadGrid on init', () => {
    when(gridServiceMock.createNewGrid(environment.gridSize)).thenReturn(of(mockGrid));
    when(gridServiceMock.getGrid()).thenReturn(of(mockGrid));

    fixture.detectChanges(); // to trigger ngOnInit

    expect(component.grid).toEqual(mockGrid);
    verify(gridServiceMock.createNewGrid(environment.gridSize)).once();
    verify(gridServiceMock.getGrid()).once();
  });

  it('should call clickCell with correct row and column when onCellClick is called', () => {
    const mockClickResponse = {
      grid: mockGrid,
      yellowCells: new Set<Cell>([{ row: 0, column: 1, value: 2 }]),
      greenCells: new Set<Cell>([{ row: 1, column: 0, value: 3 }])
    };
    when(gridServiceMock.clickCell(1, 0)).thenReturn(of(mockClickResponse));

    component.onCellClick(1, 0);

    verify(gridServiceMock.clickCell(1, 0)).once();
  });

  it('should update grid, yellowCells, and greenCells when onCellClick is successful', () => {
    const mockClickResponse = {
      grid: mockGrid,
      yellowCells: new Set<Cell>([{ row: 0, column: 1, value: 2 }]),
      greenCells: new Set<Cell>([{ row: 1, column: 0, value: 3 }])
    };

    when(gridServiceMock.clickCell(1, 0)).thenReturn(of(mockClickResponse));

    component.onCellClick(1, 0);

    expect(component.grid).toEqual(mockGrid);
    expect(component.yellowCells.size).toBe(1);
    expect(component.greenCells.size).toBe(1);

    const yellowCell = Array.from(component.yellowCells)[0];
    expect(yellowCell.row).toBe(0);
    expect(yellowCell.column).toBe(1);

    const greenCell = Array.from(component.greenCells)[0];
    expect(greenCell.row).toBe(1);
    expect(greenCell.column).toBe(0);
  });

  it('should clear yellowCells and greenCells after timeout', (done) => {
    jasmine.clock().install();
    const mockClickResponse = {
      grid: mockGrid,
      yellowCells: new Set<Cell>([{ row: 0, column: 1, value: 2 }]),
      greenCells: new Set<Cell>([{ row: 1, column: 0, value: 3 }])
    };
    when(gridServiceMock.clickCell(1, 0)).thenReturn(of(mockClickResponse));

    component.onCellClick(1, 0);

    expect(component.yellowCells.size).toBe(1);
    expect(component.greenCells.size).toBe(1);

    jasmine.clock().tick(5001);

    expect(component.yellowCells.size).toBe(0);
    expect(component.greenCells.size).toBe(0);

    jasmine.clock().uninstall();
    done();
  });

  it('should return true on evaluating yellow cell', () => {
    const yellowCell: Cell = { row: 0, column: 1, value: 2 };
    const nonYellowCell: Cell = { row: 1, column: 0, value: 3 };
    component.yellowCells.add(yellowCell);

    expect(component.isYellow(yellowCell)).toBeTruthy();
    expect(component.isYellow(nonYellowCell)).toBeFalsy();
  });

  it('should return true on evaluating green cell', () => {
    const greenCell: Cell = { row: 1, column: 0, value: 3 };
    const nonGreenCell: Cell = { row: 0, column: 1, value: 2 };
    component.greenCells.add(greenCell);

    expect(component.isGreen(greenCell)).toBeTruthy();
    expect(component.isGreen(nonGreenCell)).toBeFalsy();
  });
});
