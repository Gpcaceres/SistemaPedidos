import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'status-badge',
  imports: [CommonModule],
  template: `<span class="badge" [class.warn]="warn" [class.ok]="ok">{{ text }}</span>`,
  styles: [`.badge{padding:2px 8px;border-radius:999px;border:1px solid #ccc}
            .warn{background:#fff5e6;border-color:#f0ad4e}
            .ok{background:#e8f5e9;border-color:#5cb85c}`]
})
export class StatusBadgeComponent {
  @Input() text = '';
  @Input() warn = false;
  @Input() ok = false;
}
