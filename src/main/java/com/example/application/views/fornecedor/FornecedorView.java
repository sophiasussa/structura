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
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.util.List;

import com.example.application.model.Fornecedor;
import com.example.application.repository.DaoFornecedor;
import com.example.application.views.MainLayout;

@PageTitle("Fornecedor")
@Route(value = "my-view3", layout = MainLayout.class)
public class FornecedorView extends Composite<VerticalLayout> {

    DaoFornecedor fornecedorRepository = new DaoFornecedor();
    Grid<Fornecedor> grid = new Grid<>();
    TextField nome = new TextField();
    TextField cnpj = new TextField();
    TextField ie = new TextField();
    TextField telefone = new TextField();
    TextField email = new TextField();
    TextArea produto = new TextArea();
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

    //This method sets up two tabs
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div fornecedoresContent = createFornecedoresContent();
        tabSheet.add("Fornecedores", fornecedoresContent);

        Div addFornecedorContent = createAddFornecedorContent();
        tabSheet.add("Adicionar Fornecedor", addFornecedorContent);
    }

    //This method creates the content for the "Fornecedores" tab, which consists of a form to see all the suppliers and search for a specific suppliers
    private Div createFornecedoresContent(){
        Div fornecedorContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Fornecedor> resultados;

            if(pesquisa.isEmpty()){
                resultados = fornecedorRepository.pesquisarTodos();
            }else{
                resultados = fornecedorRepository.pesquisarFornecedor(pesquisa);
            }

            grid.setItems(resultados);
        });

        //For a better interface
        textField.setPlaceholder("Nome, CNPJ ou IE");
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

        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getNome).setHeader("Nome");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getCpf).setHeader("CNPJ");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getRg).setHeader("IE");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getTelefone).setHeader("Telefone");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getEmail).setHeader("Email");
        grid.addColumn((ValueProvider<Fornecedor, String>) Fornecedor::getDescriProdu).setHeader("Produto");


        grid.addComponentColumn(fornecedor -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir o fornecedor" + fornecedor.getNome() + "?"));

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

        grid.addItemClickListener(event -> {
            Fornecedor fornecedor = event.getItem();
            editFornecedor(fornecedor);
            tabSheet.setSelectedIndex(1);
        });

        List<Fornecedor> listaDeFornecedores = fornecedorRepository.pesquisarTodos();
        grid.setItems(listaDeFornecedores);

        layout.add(layoutRow, space, grid);
        fornecedorContentDiv.add(layout);

        return fornecedorContentDiv;
    }

    // This method creates the content for the "Adicionar Fornecedor" tab, which consists of a form to add a new supplier.
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
        
        nome = new TextField("Nome");
        cnpj = new TextField("CNPJ");
        ie = new TextField("Inscrição Estadual");
        telefone = new TextField("Telefone");
        email = new TextField("Email");
        produto = new TextArea("Descrição do Produto Fornecido");

        cnpj.addValueChangeListener(event -> {
            String value = event.getValue().replaceAll("[^\\d]", "");
            if (value.length() <= 11) {
                value = value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            }
            cnpj.setValue(value);
        });
        cnpj.setMaxLength(14);

        ie.addValueChangeListener(event -> {
            String value = event.getValue().replaceAll("[^\\d]", "");
            ie.setValue(value);
        });
        ie.setMaxLength(12);

        Button saveButton = new Button("Salvar", event -> {
            if (nome.isEmpty() || telefone.isEmpty()) {
                Notification.show("Preencha os campos obrigatórios: Nome e Telefone", 3000, Notification.Position.MIDDLE);
                return;
            }

            String nomeFornecedor = nome.getValue();
            String cnpjFornecedor = cnpj.isEmpty() ? "" : cnpj.getValue();
            String ieFornecedor = ie.isEmpty() ? "" : ie.getValue();
            String telefoneFornecedor = telefone.getValue();
            String emailFornecedor = email.getValue();
            String descriFornecedor = produto.getValue();

            Fornecedor fornecedor = new Fornecedor(nomeFornecedor, cnpjFornecedor, ieFornecedor, telefoneFornecedor, emailFornecedor, descriFornecedor);
            fornecedor.setId(fornecedorId);

            boolean sucesso;
            if (fornecedorId != null && fornecedorId > 0) {
                sucesso = fornecedorRepository.alterar(fornecedor);
                if (sucesso) {
                    Notification.show("Fornecedor atualizado com sucesso!");
                } else {
                    Notification.show("Erro ao atualizar o fornecedor", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = fornecedorRepository.inserir(fornecedor);
                if (sucesso) {
                    Notification.show("Fornecedor salvo com sucesso!");
                } else {
                    Notification.show("Erro ao salvar fornecedor", 3000, Notification.Position.MIDDLE);
                }
            }

            if (sucesso) {
                clearForm();
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        // Estilos e classes de CSS
        nome.addClassName("rounded-text-field");
        cnpj.addClassName("rounded-text-field");
        ie.addClassName("rounded-text-field");
        telefone.addClassName("rounded-text-field");
        email.addClassName("rounded-text-field");
        produto.addClassName("rounded-text-field");
        nome.setRequiredIndicatorVisible(true);
        telefone.setRequiredIndicatorVisible(true);
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");
        layout3.setAlignItems(FlexComponent.Alignment.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");

        // Adiciona componentes ao layout
        formLayout2Col.add(nome, cnpj, ie, produto, email, telefone);
        layout2.add(formLayout2Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addFornecedorContentDiv.add(space1, layout);

        return addFornecedorContentDiv;
    }

    private void refreshGrid(){
        List<Fornecedor> fornecedores = fornecedorRepository.pesquisarTodos();
        grid.setItems(fornecedores);
    }

    private void deleteFornecedor(Fornecedor fornecedor){
        boolean sucess = fornecedorRepository.excluir(fornecedor);
        if(sucess){
            refreshGrid();
        }else{
            System.out.println("Erro ao excluir cliente");
        }
    }

    private void editFornecedor(Fornecedor fornecedor){
        fornecedorId = fornecedor.getId();
        nome.setValue(fornecedor.getNome());
        cnpj.setValue(String.valueOf(fornecedor.getCpf()));
        ie.setValue(String.valueOf(fornecedor.getRg()));
        telefone.setValue(String.valueOf(fornecedor.getTelefone()));
        email.setValue(String.valueOf(fornecedor.getEmail()));
        produto.setValue(String.valueOf(fornecedor.getDescriProdu()));
    }

    private void clearForm(){
        fornecedorId = null;
        nome.clear();
        cnpj.clear();
        ie.clear();
        telefone.clear();
        email.clear();
        produto.clear();
    }
}
