import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

// =====================================================
// LOGIN RESPONSE
// =====================================================

export interface LoginResponse {

  message: string;

  userId: number | null;

  email: string | null;

  role: string | null;

  fullName: string | null;

}


// =====================================================
// AUTH SERVICE
// =====================================================

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;
  constructor(
    private http: HttpClient
  ) {}


  // ===================================================
  // REGISTER
  // ===================================================

  register(
    user: any
  ): Observable<string> {

    return this.http.post(
      `${this.apiUrl}/register`,
      user,
      {
        responseType: 'text'
      }
    );

  }


  // ===================================================
  // LOGIN
  // ===================================================

  login(
    username: string,
    password: string
  ): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      {
        username: username,
        password: password
      }
    );

  }

}