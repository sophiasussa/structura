package com.example.application.views;

import com.example.application.views.cliente.ClienteView;
import com.example.application.views.fornecedor.FornecedorView;
import com.example.application.views.funcionario.FuncionarioView;
import com.example.application.views.ordemServico.OrdemServicoView;
import com.example.application.views.home.HomeView;
import com.example.application.views.agenda.AgendaView;
import com.example.application.views.produto.ProdutoView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout{

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }
    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        HorizontalLayout headerLayout = new HorizontalLayout(toggle, viewTitle);
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        headerLayout.expand(viewTitle);
        headerLayout.setWidthFull();

        addToNavbar(true, headerLayout);
    }

    private void addDrawerContent() {
        Span appName = new Span("Menu");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        SideNav mainNavigation = createNavigation();
        SideNav secondaryNavigation = createNavigation2();
        SideNav terceiroNavigation = createNavigation3();
        SideNav quartoNavigation = createNavigation4();
    
        VerticalLayout navLayout = new VerticalLayout(mainNavigation, secondaryNavigation);
        navLayout.setSpacing(false);
        navLayout.setPadding(false);
        
        Scroller scroller = new Scroller(navLayout);

        VerticalLayout drawerContent = new VerticalLayout(header, mainNavigation, secondaryNavigation, terceiroNavigation, quartoNavigation);
        drawerContent.setSpacing(true);
        drawerContent.setPadding(true);
        drawerContent.setSizeFull();
        drawerContent.setAlignItems(FlexComponent.Alignment.STRETCH);

        drawerContent.setFlexGrow(1, scroller);
    
        addToDrawer(drawerContent);
    }
    
    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Home", HomeView.class, VaadinIcon.HOME.create()));

        return nav;
    }

    private SideNav createNavigation2() {
        SideNav nav = new SideNav();

        nav.setLabel("Registros");
        nav.setCollapsible(true);
        nav.setExpanded(false);
        nav.addItem(new SideNavItem("Cliente", ClienteView.class, VaadinIcon.GROUP.create()));
        nav.addItem(new SideNavItem("Fornecedor", FornecedorView.class, VaadinIcon.HANDSHAKE.create()));
        nav.addItem(new SideNavItem("Funcionário", FuncionarioView.class, VaadinIcon.MALE.create()));
        nav.addItem(new SideNavItem("Produto", ProdutoView.class, VaadinIcon.PACKAGE.create()));

        return nav;
    }

    private SideNav createNavigation3() {
        SideNav nav = new SideNav();

        nav.setLabel("Serviços");
        nav.setCollapsible(true);
        nav.setExpanded(false);
        nav.addItem(new SideNavItem("Ordem de Serviço", OrdemServicoView.class, VaadinIcon.CLIPBOARD_TEXT.create()));

        return nav;
    }

    private SideNav createNavigation4() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Agenda", AgendaView.class, VaadinIcon.BOOK.create()));

        return nav;
    }



    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }
}