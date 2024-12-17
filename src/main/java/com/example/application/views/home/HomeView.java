package com.example.application.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;


@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends Composite<VerticalLayout> {

    public HomeView() {
       VerticalLayout layoutColumn2 = new VerticalLayout();
        H1 h12 = new H1();
        Hr hr4 = new Hr();
        H3 h34 = new H3();
        Grid minimalistGrid4 = new Grid();
        minimalistGrid4.setAllRowsVisible(true);
        Hr hr5 = new Hr();
        H3 h35 = new H3();
        Grid minimalistGrid5 = new Grid();
        minimalistGrid5.setAllRowsVisible(true);
        Hr hr6 = new Hr();
        H3 h36 = new H3();
        Grid minimalistGrid6 = new Grid();
        minimalistGrid6.setAllRowsVisible(true);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h12.setText("Structura");
        h12.setWidth("max-content");
        h34.setText("Agendamentos para Hoje");
        h34.setWidth("max-content");
        minimalistGrid4.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS);
        minimalistGrid4.setWidth("100%");
        minimalistGrid4.getStyle().set("flex-grow", "0");
        setGridSampleData(minimalistGrid4);
        h35.setText("OS para Hoje");
        h35.setWidth("max-content");
        minimalistGrid5.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS);
        minimalistGrid5.setWidth("100%");
        minimalistGrid5.getStyle().set("flex-grow", "0");
        setGridSampleData(minimalistGrid5);
        h36.setText("Produtos com Estoque Baixo");
        h36.setWidth("max-content");
        minimalistGrid6.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS);
        minimalistGrid6.setWidth("100%");
        minimalistGrid6.getStyle().set("flex-grow", "0");
        setGridSampleData(minimalistGrid6);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h12);
        layoutColumn2.add(hr4);
        layoutColumn2.add(h34);
        layoutColumn2.add(minimalistGrid4);
        layoutColumn2.add(hr5);
        layoutColumn2.add(h35);
        layoutColumn2.add(minimalistGrid5);
        layoutColumn2.add(hr6);
        layoutColumn2.add(h36);
        layoutColumn2.add(minimalistGrid6);
    }

    private void setGridSampleData(Grid grid) {
    }
}
