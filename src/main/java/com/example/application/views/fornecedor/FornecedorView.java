package com.example.application.views.fornecedor;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.util.Collections;
import java.util.List;

import com.example.application.model.Fornecedor;
import com.example.application.views.MainLayout;
import com.example.application.controller.FornecedorController;

@PageTitle("Fornecedor")
@Route(value = "my-view3", layout = MainLayout.class)
public class FornecedorView extends Composite<VerticalLayout> {

    private FornecedorController fornecedorController = new FornecedorController();
    private Grid<Fornecedor> grid = new Grid<>();
    private TextField empresa = new TextField();
    private TextField vendedor = new TextField();
    private TextField cnpj = new TextField();
    private TextField ie = new TextField();
    private TextField telefone = new TextField();
    private TextField email = new TextField();
    private TextArea produto = new TextArea();
    private Long fornecedorId;
    private TabSheet tabSheet;

    public FornecedorView() {
        tabSheet = new TabSheet();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        getContent().add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div fornecedoresContent = createFornecedoresContent();
        tabSheet.add("Fornecedores", fornecedoresContent);
        Div addFornecedorContent = createAddFornecedorContent();
        tabSheet.add("Adicionar Fornecedor", addFornecedorContent);
    }

    private Div createFornecedoresContent(){
        Div fornecedorContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        textField.setPlaceholder("Empresa, CNPJ ou IE");
        textField.setWidth("250px");
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setAlignItems(Alignment.END);
        layoutRow.setJustifyContentMode(JustifyContentMode.END);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("70px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.setIcon(VaadinIcon.SEARCH.create());
        buttonPrimary.getStyle().set("border-radius", "50%");
        textField.addClassName("rounded-text-field");
        layoutRow.add(textField, buttonPrimary);
        grid.setAllRowsVisible(true);
        grid.addClassName("borderless-grid");

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Fornecedor> resultados;
            if(pesquisa.isEmpty()){
                resultados = fornecedorController.pesquisarTodos();
            }else{
                resultados = fornecedorController.pesquisarFornecedor(pesquisa);
            }
            grid.setItems(resultados);
        });

        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getEmpresa).setHeader("Empresa");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getVendedor).setHeader("Vendedor");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getTelefone).setHeader("Telefone");
        
        grid.addComponentColumn(fornecedor -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir o fornecedor" + fornecedor.getEmpresa() + "?"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deleteFornecedor(fornecedor);
                    confirm.close();
                });
                Button cancel = new Button("Cancelar", event -> confirm.close());

                confirm.getFooter().add(confirmButton, cancel);
                confirm.add(content);
                confirm.open();
            });
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return delete;
        }).setHeader("Ações");
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createPersonDetailsRenderer());

        grid.addItemClickListener(event -> {
            Fornecedor fornecedor = event.getItem();
            grid.setDetailsVisible(fornecedor, !grid.isDetailsVisible(fornecedor));
        });

        grid.addItemDoubleClickListener(event -> {
            Fornecedor fornecedor = event.getItem();
            editFornecedor(fornecedor);
            tabSheet.setSelectedIndex(1);
        });

        List<Fornecedor> listaDeFornecedores = fornecedorController.pesquisarTodos();
        if (listaDeFornecedores == null) {
            listaDeFornecedores = Collections.emptyList();
        }
        grid.setItems(listaDeFornecedores);

        layout.add(layoutRow, space, grid);
        fornecedorContentDiv.add(layout);
        return fornecedorContentDiv;
    }

    private Div createAddFornecedorContent() {
        Div addFornecedorContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");
        Div space1 = new Div();
        space1.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        empresa = new TextField("Empresa");
        empresa.setMaxLength(100);
        vendedor = new TextField("Vendedor");
        vendedor.setMaxLength(100);
        cnpj = new TextField("CNPJ");
        ie = new TextField("Inscrição Estadual");
        telefone = new TextField("Telefone");
        email = new TextField("Email");
        email.setMaxLength(100);
        produto = new TextArea("Descrição do Produto Fornecido");
        produto.setMaxLength(255);
        empresa.addClassName("rounded-text-field");
        vendedor.addClassName("rounded-text-field");
        cnpj.addClassName("rounded-text-field");
        ie.addClassName("rounded-text-field");
        telefone.addClassName("rounded-text-field");
        email.addClassName("rounded-text-field");
        produto.addClassName("rounded-text-field");
        empresa.setRequiredIndicatorVisible(true);
        telefone.setRequiredIndicatorVisible(true);
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");
        layout3.setAlignItems(FlexComponent.Alignment.END);

        cnpj.addBlurListener(event -> {
            String value = cnpj.getValue().replaceAll("[^\\d]", "");

            if (value.length() <= 11) {
                value = value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            } else {
                value = value.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
            }
            cnpj.setValue(value);
        });
        cnpj.setMaxLength(18);

        ie.addBlurListener(event -> {
            String value = ie.getValue().replaceAll("[^\\d]", "");

            if (value.length() == 9) {
                value = value.replaceAll("(\\d{2})(\\d{3})(\\d{3})", "$1.$2.$3");
            } else if (value.length() == 12) {
                value = value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{3})", "$1.$2.$3.$4");
            }
            ie.setValue(value);
        });
        ie.setMaxLength(15);

        telefone.addBlurListener(event -> {
            String value = telefone.getValue().replaceAll("[^\\d]", "");
        
            if (value.length() == 10) {
                value = value.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
            } else if (value.length() == 11) {
                value = value.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
            }
        
            telefone.setValue(value);
        });
        telefone.setMaxLength(15);

        Button saveButton = new Button("Salvar", event -> {
            if (empresa.isEmpty() || telefone.isEmpty()) {
                Notification.show("Preencha os campos obrigatórios: Empresa e Telefone", 3000, Notification.Position.MIDDLE);
                return;
            }

            String nomeFornecedor = empresa.getValue();
            String vendedorFornecedor = vendedor.getValue();
            String cnpjFornecedor = cnpj.isEmpty() ? "" : cnpj.getValue();
            String ieFornecedor = ie.isEmpty() ? "" : ie.getValue();
            String telefoneFornecedor = telefone.getValue();
            String emailFornecedor = email.getValue();
            String descriFornecedor = produto.getValue();

            Fornecedor fornecedor = new Fornecedor(nomeFornecedor, vendedorFornecedor, cnpjFornecedor, ieFornecedor, telefoneFornecedor, emailFornecedor, descriFornecedor);
            fornecedor.setId(fornecedorId);

            boolean sucesso;
            if (fornecedorId != null && fornecedorId > 0) {
                sucesso = fornecedorController.alterar(fornecedor);
                if (sucesso) {
                    Notification.show("Fornecedor atualizado com sucesso!");
                    clearForm();
                } else {
                    Notification.show("Erro ao atualizar o fornecedor", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = fornecedorController.inserir(fornecedor);
                if (sucesso) {
                    Notification.show("Fornecedor salvo com sucesso!");
                    clearForm();
                } else {
                    Notification.show("Erro ao salvar fornecedor", 3000, Notification.Position.MIDDLE);
                }
            }
            if (sucesso) {
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        formLayout2Col.add(empresa, vendedor, cnpj, ie, produto, email, telefone);
        layout2.add(formLayout2Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addFornecedorContentDiv.add(space1, layout);
        return addFornecedorContentDiv;
    }

    private void refreshGrid(){
        List<Fornecedor> fornecedores = fornecedorController.pesquisarTodos();
        grid.setItems(fornecedores);
    }

    private void deleteFornecedor(Fornecedor fornecedor){
        boolean sucess = fornecedorController.excluir(fornecedor);
        if(sucess){
            refreshGrid();
        }else{
            Notification.show("Erro ao excluir fornecedor", 3000, Notification.Position.MIDDLE);
        }
    }

    private void editFornecedor(Fornecedor fornecedor){
        fornecedorId = fornecedor.getId();
        empresa.setValue(fornecedor.getEmpresa());
        vendedor.setValue(fornecedor.getVendedor());
        cnpj.setValue(String.valueOf(fornecedor.getCnpj()));
        ie.setValue(String.valueOf(fornecedor.getIe()));
        telefone.setValue(String.valueOf(fornecedor.getTelefone()));
        email.setValue(String.valueOf(fornecedor.getEmail()));
        produto.setValue(String.valueOf(fornecedor.getDescriProdutos()));
    }

    private void clearForm(){
        fornecedorId = null;
        empresa.clear();
        vendedor.clear();
        cnpj.clear();
        ie.clear();
        telefone.clear();
        email.clear();
        produto.clear();
    }

    private static ComponentRenderer<HorizontalLayout, Fornecedor> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(fornecedor -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setSpacing(true);
            detailsLayout.setPadding(true);
            detailsLayout.addClassName("details-layout");
    
            TextField cpfField = new TextField("CNPJ");
            cpfField.setValue(fornecedor.getCnpj());
            cpfField.setReadOnly(true);
            cpfField.addClassName("rounded-text-field");
    
            TextField rgField = new TextField("IE");
            rgField.setValue(fornecedor.getIe());
            rgField.setReadOnly(true);
            rgField.addClassName("rounded-text-field");

            TextField emailField = new TextField("Email");
            emailField.setValue(fornecedor.getEmail());
            emailField.setReadOnly(true);
            emailField.addClassName("rounded-text-field");
    
            TextArea produtoArea = new TextArea("Produtos");
            produtoArea.setValue(fornecedor.getDescriProdutos().toString());
            produtoArea.setReadOnly(true);
            produtoArea.addClassName("rounded-text-field");
    
            detailsLayout.add(cpfField, rgField, emailField ,produtoArea);
            return detailsLayout;
        });
    }
}