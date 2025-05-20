import { Component } from '@angular/core';
import {GridComponent} from './components/grid/grid.component';

@Component({
  selector: 'app-root',
  imports: [GridComponent],
  template: '<app-grid></app-grid>',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'grid-game';
}
