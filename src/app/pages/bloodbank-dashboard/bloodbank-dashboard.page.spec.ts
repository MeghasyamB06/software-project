import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BloodbankDashboardPage } from './bloodbank-dashboard.page';

describe('BloodbankDashboardPage', () => {
  let component: BloodbankDashboardPage;
  let fixture: ComponentFixture<BloodbankDashboardPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(BloodbankDashboardPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
