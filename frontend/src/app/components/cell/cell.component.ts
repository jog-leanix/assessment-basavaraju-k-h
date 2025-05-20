import { Component, Input } from '@angular/core';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-cell',
  templateUrl: './cell.component.html',
  styleUrls: ['./cell.component.css'],
  imports: [
    NgClass
  ]
})
export class CellComponent {
  @Input() value!: number;
  @Input() isYellow: boolean = false;
  @Input() isGreen: boolean = false;
}
