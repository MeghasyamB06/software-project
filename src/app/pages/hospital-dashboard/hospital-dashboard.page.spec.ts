import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HospitalDashboardPage } from './hospital-dashboard.page';

describe('HospitalDashboardPage', () => {
  let component: HospitalDashboardPage;
  let fixture: ComponentFixture<HospitalDashboardPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(HospitalDashboardPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
