import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ComponentsRoutingModule } from './components-routing.module';

import { HomeComponent } from './home/home.component';
import { WelcomeComponent } from './welcome/welcome.component';

@NgModule({
  declarations: [HomeComponent, WelcomeComponent],
  imports: [
    CommonModule,
    ComponentsRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class ComponentsModule { }
