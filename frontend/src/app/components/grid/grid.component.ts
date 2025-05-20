import {Component, OnInit} from '@angular/core';
import {Cell} from '../../models/Cell';
import {GridService} from '../../services/grid.service';
import {CellComponent} from '../cell/cell.component';
import {NgForOf} from '@angular/common';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css'],
  imports: [
    CellComponent,
    NgForOf
  ]
})
export class GridComponent implements OnInit {
  grid: Cell[][] = [];
  yellowCells = new Set<Cell>;
  greenCells = new Set<Cell>();

  constructor(private gridService: GridService) {
  }

  ngOnInit(): void {
    this.createGrid();
    this.loadGrid();
  }

  createGrid() {
    this.gridService.createNewGrid(environment.gridSize).subscribe(grid => {
      this.grid = grid;
    });
  }

  loadGrid() {
    this.gridService.getGrid().subscribe(grid => {
      this.grid = grid;
    });
  }

  onCellClick(row: number, col: number) {
    this.gridService.clickCell(row, col).subscribe(result => {
      this.grid = result.grid;

      this.yellowCells.clear();
      this.greenCells.clear();
      result.yellowCells.forEach((cell) => this.yellowCells.add(cell));
      result.greenCells.forEach((cell) => this.greenCells.add(cell));

      setTimeout(() => this.yellowCells.clear(), 5000);
      setTimeout(() => this.greenCells.clear(), 5000);
    });
  }

  isYellow(cell: Cell): boolean {
    return Array.from(this.yellowCells).some(storedCell =>
      storedCell.row === cell.row && storedCell.column === cell.column
    );
  }

  isGreen(cell: Cell): boolean {
    return Array.from(this.greenCells).some(storedCell =>
      storedCell.row === cell.row && storedCell.column === cell.column
    );
  }

  resetGrid() {
    this.gridService.resetGrid().subscribe(result => {
      this.grid = result.grid;
      this.yellowCells.clear();
      this.greenCells.clear()
    });
  }
}
