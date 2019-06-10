import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private isAdmin: boolean = false;

  constructor() { }

  public isAdministrator() {
    return this.isAdmin;
  }

  public userIsAdmin() {
    this.isAdmin = true;
  }

  public userInNotAdmin() {
    this.isAdmin = false;
  }
}
