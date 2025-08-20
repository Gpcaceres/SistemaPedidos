import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { TrackingState } from '../models/tracking.model';

@Injectable({ providedIn: 'root' })
export class TrackingService {
  private base = environment.apiBase + environment.endpoints.tracking;
  constructor(private http: HttpClient) {}
  getState(orderId: number|string): Observable<TrackingState> {
    return this.http.get<TrackingState>(`${this.base}/${orderId}`);
  }
}
