import { Component } from '@angular/core';

@Component({
  selector: 'app-operator-dashboard',
  templateUrl: './operator-dashboard.component.html',
  styleUrls: ['./operator-dashboard.component.css']
})
export class OperatorDashboardComponent {
  activeTab = 'ordenes-activas';

  setActive(tab: string) {
    this.activeTab = tab;
  }
}
