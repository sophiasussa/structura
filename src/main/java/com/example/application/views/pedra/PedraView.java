package com.example.application.views.pedra;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.example.application.model.Cliente;
import com.example.application.model.Cor;
import com.example.application.model.Pedra;
import com.example.application.model.Material;
import com.example.application.model.UnidMedida;
import com.example.application.repository.DaoMaterial;
import com.example.application.repository.DaoPedra;
import com.example.application.repository.DaoUnidMedida;
import com.example.application.views.MainLayout;
import com.example.application.repository.DaoCor;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Pedras")
@Route(value = "my-view4", layout = MainLayout.class)
public class PedraView extends VerticalLayout{

    DaoPedra pedraRepository = new DaoPedra();
    DaoCor corRepository = new DaoCor();
    DaoMaterial materialRepository = new DaoMaterial();
    DaoUnidMedida unidMedidaRepository = new DaoUnidMedida();
    Grid<Pedra> grid = new Grid(Pedra.class, false);
    TextField nome = new TextField("Nome");
    TextField modelo = new TextField("Modelo");
    TextField quantidadeAtual = new TextField("Quantidade Atual");
    TextField quantidadeMinima = new TextField("Quantidade Mínima");
    TextField custoUnitario = new TextField("Custo Unitário");
    Button buttonTertiary = new Button();
    Button buttonTertiary2 = new Button();
    Button buttonTertiary3 = new Button();
    ComboBox<Cor> cor = new ComboBox<>("Cor");
    ComboBox<Material> material = new ComboBox<>("Material");
    ComboBox<UnidMedida> unidMedida = new ComboBox<>("Unidade de Medida");
    private Long pedraId;
    private TabSheet tabSheet;

    public PedraView() {
        tabSheet = new TabSheet();
        this.setWidth("100%");
        this.getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        this.add(tabSheet);
    }

    //This method sets up two tabs
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div pedrasContent = createPedrasContent();
        tabSheet.add("Pedras", pedrasContent);

        Div addPedrasContent = createAddPedrasContent();
        tabSheet.add("Adicionar Pedra", addPedrasContent);
    }

    //This method creates the content for the "Pedras" tab, which consists of a form to see all the pedras and search for a specific pedra
    private Div createPedrasContent(){
        Div pedrasContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Pedra> resultados;

            if(pesquisa.isEmpty()){
                resultados = pedraRepository.pesquisarTodos();
            }else{
                resultados = pedraRepository.pesquisarPedra(pesquisa);
            }

            grid.setItems(resultados);
        });

        //For a better interface
        textField.setPlaceholder("Nome");
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

        buttonPrimary.addClickListener(pedra -> {
            Notification.show("Pesquisar por: " + textField.getValue());
        });

        grid = createGrid();

        layout.add(layoutRow, space, grid);
        pedrasContentDiv.add(layout);

        return pedrasContentDiv;
    }

   //This method creates the content for the "Adicionar Pedra" tab, which consists of a form to add a new Pedra.
    private Div createAddPedrasContent(){
        Div addPedrasContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");
        Div space1 = new Div();
        space1.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        FormLayout formLayout3Col = new FormLayout();
        nome = new TextField("Nome");
        modelo = new TextField("Modelo");
        quantidadeAtual = new TextField("Quantidade Atual");
        quantidadeMinima = new TextField("Quantidade Mínima");
        custoUnitario = new TextField("Custo Unitário");
        setComboBoxCorData(cor);
        cor.setWidth("min-content");
        buttonTertiary.setText("+ Cor");
        buttonTertiary.setWidth("min-content");
        buttonTertiary.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonTertiary.addClickListener(event -> openDialog1());
        setComboBoxMaterialData(material);
        material.setWidth("min-content");
        buttonTertiary2.setText("+ Material");
        buttonTertiary2.setWidth("min-content");
        buttonTertiary2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonTertiary2.addClickListener(event -> openDialog2());
        setComboBoxUnidMedidaData(unidMedida);
        unidMedida.setWidth("min-content");
        buttonTertiary3.setText("+ Unidade de Medida");
        buttonTertiary3.setWidth("min-content");
        buttonTertiary3.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonTertiary3.addClickListener(event -> openDialog3());

        Button saveButton = new Button("Salvar", event -> {
            if (nome.isEmpty()) {
                Notification.show("Preencha o campo obrigatório: Nome", 3000, Notification.Position.MIDDLE);
                return;
            }
            String nomePedra = nome.getValue();
            String modeloPedra = modelo.isEmpty() ? "" : modelo.getValue();
            int quantidadeAtualPedra = quantidadeAtual.isEmpty() ? 0 : Integer.parseInt(quantidadeAtual.getValue());
            int quantidadeMinimaPedra = quantidadeMinima.isEmpty() ? 0 : Integer.parseInt(quantidadeMinima.getValue());
            BigDecimal custoUnitarioPedra = custoUnitario.isEmpty() ? BigDecimal.ZERO : new BigDecimal(custoUnitario.getValue());
            Cor corPedra = cor.isEmpty() ? null : cor.getValue();
            Material materialPedra = material.isEmpty() ? null : material.getValue();
            UnidMedida unidMedidaPedra = unidMedida.isEmpty() ? null : unidMedida.getValue();
        
            Pedra pedra = new Pedra(nomePedra, modeloPedra, quantidadeAtualPedra, quantidadeMinimaPedra, custoUnitarioPedra, corPedra, materialPedra, unidMedidaPedra);
            pedra.setId(pedraId);

            boolean sucesso;
            if (pedraId != null && pedraId > 0) {
                sucesso = pedraRepository.alterar(pedra);
                if (sucesso) {
                    Notification.show("Pedra atualizado com sucesso!");
                } else {
                    Notification.show("Erro ao atualizar o pedra", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = pedraRepository.inserir(pedra);
                if (sucesso) {
                    Notification.show("Pedra salva com sucesso!");
                } else {
                    Notification.show("Erro ao salvar o pedra", 3000, Notification.Position.MIDDLE);
                }
            }

            if (sucesso) {
                clearForm();
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        //For a better interface
        nome.addClassName("rounded-text-field");
        modelo.addClassName("rounded-text-field");
        quantidadeAtual.addClassName("rounded-text-field");
        quantidadeMinima.addClassName("rounded-text-field");
        custoUnitario.addClassName("rounded-text-field");
        cor.addClassName("rounded-text-field");
        material.addClassName("rounded-text-field");
        unidMedida.addClassName("rounded-text-field");
        nome.setRequiredIndicatorVisible(true);
        layout3.setAlignItems(FlexComponent.Alignment.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");

        formLayout3Col.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 3)
        );
        
        formLayout2Col.add(nome,modelo);
        formLayout3Col.add(custoUnitario,quantidadeAtual,quantidadeMinima,cor, material ,unidMedida, buttonTertiary, buttonTertiary2, buttonTertiary3);
        layout2.add(formLayout2Col, formLayout3Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addPedrasContentDiv.add(space1, layout);

        return addPedrasContentDiv;
    }


    private Grid<Pedra> createGrid() {
        grid = new Grid<>(Pedra.class, false);
        grid.addClassName("borderless-grid");
        grid.setAllRowsVisible(true);

        grid.addColumn(Pedra::getNome).setHeader("Nome").setSortable(true);
        grid.addColumn(pedra -> 
            pedra.getMaterial() != null && pedra.getMaterial().getNome() != null ? 
            pedra.getMaterial().getNome() : "Sem Material"
        ).setHeader("Material").setSortable(true);
        
        grid.addColumn(pedra -> 
            pedra.getCor() != null && pedra.getCor().getNome() != null ? 
            pedra.getCor().getNome() : "Sem Cor"
        ).setHeader("Cor").setSortable(true);

        grid.addColumn(pedra -> 
            pedra.getUnidMedida() != null && pedra.getUnidMedida().getNome() != null ? 
            pedra.getUnidMedida().getNome() : "Sem Unidade de Medida"
        ).setHeader("Unidade de Medida").setSortable(true);

        grid.addComponentColumn(pedra -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir essa pedra " + pedra.getNome() + "?"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deletePedra(pedra);
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
        grid.setItemDetailsRenderer(createPedraDetailsRenderer());

        grid.addItemClickListener(event -> {
            Pedra pedra = event.getItem();
            grid.setDetailsVisible(pedra, !grid.isDetailsVisible(pedra));
        });

        grid.addItemDoubleClickListener(event -> {
            Pedra pedra = event.getItem();
            editPedra(pedra);
            tabSheet.setSelectedIndex(1);
        });

        grid.setItems(pedraRepository.pesquisarTodos());

        return grid;
    }

    private void setComboBoxCorData(ComboBox<Cor> comboBox) {
        List<Cor> cores = corRepository.pesquisarTodos();
        comboBox.setItems(cores);
        comboBox.setItemLabelGenerator(cor -> cor.getNome());
    }

    private void setComboBoxMaterialData(ComboBox<Material> comboBox) {
        List<Material> materiais = materialRepository.pesquisarTodos();
        comboBox.setItems(materiais);
        comboBox.setItemLabelGenerator(material -> material.getNome());
    }

    private void setComboBoxUnidMedidaData(ComboBox<UnidMedida> comboBox) {
        List<UnidMedida> medidas = unidMedidaRepository.pesquisarTodos();
        comboBox.setItems(medidas);
        comboBox.setItemLabelGenerator(unidMedida -> unidMedida.getNome());
    }

    private void refreshGrid(){
        List<Pedra> pedras = pedraRepository.pesquisarTodos();
        grid.setItems(pedras);
    }

    private void deletePedra(Pedra pedra){
        boolean success = pedraRepository.excluir(pedra);
        if(success){
            refreshGrid();
        }else{
            System.out.println("Erro ao excluir pedra");
        }
    }

    private void editPedra(Pedra pedra) {
        pedraId = pedra.getId();
        nome.setValue(pedra.getNome());
        modelo.setValue(pedra.getModelo() != null ? pedra.getModelo() : "");
        quantidadeAtual.setValue(String.valueOf(pedra.getQuantidadeAtual()));
        quantidadeMinima.setValue(String.valueOf(pedra.getQuantidadeMinima()));
        custoUnitario.setValue(pedra.getCustoUnitario() != null ? pedra.getCustoUnitario().toString() : "");
        cor.setValue(pedra.getCor());
        material.setValue(pedra.getMaterial());
        unidMedida.setValue(pedra.getUnidMedida());
    }

    private void clearForm() {
        pedraId = null;
        nome.clear();
        modelo.clear();
        quantidadeAtual.clear();
        quantidadeMinima.clear();
        custoUnitario.clear();
        cor.clear();
        material.clear();
        unidMedida.clear();
    }

    private static ComponentRenderer<HorizontalLayout, Pedra> createPedraDetailsRenderer() {
        return new ComponentRenderer<>(pedra -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setSpacing(true);
            detailsLayout.setPadding(true);
            detailsLayout.addClassName("details-layout");

            TextField modeloField = new TextField("Modelo");
            modeloField.setValue(pedra.getModelo() != null ? pedra.getModelo() : "");
            modeloField.setReadOnly(true);
            modeloField.addClassName("rounded-text-field");

            TextField quantidadeAtualField = new TextField("Quantidade Atual");
            quantidadeAtualField.setValue(String.valueOf(pedra.getQuantidadeAtual()));
            quantidadeAtualField.setReadOnly(true);
            quantidadeAtualField.addClassName("rounded-text-field");

            TextField quantidadeMinimaField = new TextField("Quantidade Mínima");
            quantidadeMinimaField.setValue(String.valueOf(pedra.getQuantidadeMinima()));
            quantidadeMinimaField.setReadOnly(true);
            quantidadeMinimaField.addClassName("rounded-text-field");

            TextField custoUnitarioField = new TextField("Custo Unitário");
            custoUnitarioField.setValue(pedra.getCustoUnitario() != null ? pedra.getCustoUnitario().toString() : "");
            custoUnitarioField.setReadOnly(true);
            custoUnitarioField.addClassName("rounded-text-field");

            detailsLayout.add(modeloField, quantidadeAtualField, quantidadeMinimaField, custoUnitarioField);

            return detailsLayout;
        });
    }

    private void openDialog1() {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");

        Grid<Cor> grid = new Grid<>(Cor.class);
        grid.setColumns("nome");

        List<Cor> cores = corRepository.pesquisarTodos();
        grid.setItems(cores);

        Editor<Cor> editor = grid.getEditor();
        Binder<Cor> binder = new Binder<>(Cor.class);
        editor.setBinder(binder);

        TextField nomeEditor = new TextField();
        binder.forField(nomeEditor).bind(Cor::getNome, Cor::setNome);
        grid.getColumnByKey("nome").setEditorComponent(nomeEditor);

        grid.addItemDoubleClickListener(event -> editor.editItem(event.getItem()));
        editor.addCloseListener(event -> grid.getDataProvider().refreshItem(event.getItem()));
        
        grid.addComponentColumn(cor -> {
            Button alterarButton = new Button("Alterar", new Icon(VaadinIcon.COG));
            alterarButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.save();
                    Cor editedCor = editor.getItem();
                    if (corRepository.alterar(editedCor)) {
                        Notification notification = new Notification(
                                "Cor atualizado com sucesso.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                        
                        cores.clear();
                        cores.addAll(corRepository.pesquisarTodos());
                        grid.getDataProvider().refreshAll();
                    } else {
                        Notification notification = new Notification(
                                "Erro ao atualizar. Verifique se todos os dados foram preenchidos.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                    }
                } else {
                    editor.editItem(cor);
                    nomeEditor.focus();
                }
            });
            return alterarButton;
        }).setHeader("Alterar");
        
        editor.addSaveListener(event -> {
            grid.getDataProvider().refreshItem(event.getItem());
        });

        grid.addComponentColumn(cor -> {
            Button deletarButton = new Button(new Icon(VaadinIcon.TRASH));
            deletarButton.addClickListener(e -> {
                if (corRepository.excluir(cor)) {
                    Notification notification = new Notification(
                            "Cor deletada com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                    
                    cores.clear();
                    cores.addAll(corRepository.pesquisarTodos());
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification notification = new Notification(
                            "Erro ao deletar. Tente novamente.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
            return deletarButton;
        }).setHeader("Deletar");
    

        Button confirmarButton = new Button("Salvar", event -> {
            if(nomeField.isEmpty()){
                Notification notification = new Notification(
                    "Erro: O nome não pode estar vazio.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            } else {
                Cor cor = new Cor();
                cor.setNome(nomeField.getValue());
                if (corRepository.inserir(cor) == true) {
                    Notification notification = new Notification(
                            "Cor salva com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    nomeField.clear();
                    cores.clear();
                    cores.addAll(corRepository.pesquisarTodos());
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification notification = new Notification(
                            "Erro ao salvar. Verifique se todos os dados foram preenchidos.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            }
        });
        Button cancelarButton = new Button("Fechar", event -> dialog.close());

        cancelarButton.getStyle()
            .set("background-color", "#FF0000")
            .set("color", "#FFFFFF")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.2)")
            .set("cursor", "pointer");

        confirmarButton.getStyle()
            .set("background-color", "#228B22")
            .set("color", "#FFFFFF")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.2)")
            .set("cursor", "pointer");

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelarButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setPadding(false);
        buttonLayout.setSpacing(true);

        formLayout.add(nomeField, confirmarButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, grid, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openDialog2() {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");

        Grid<Material> grid = new Grid<>(Material.class);
        grid.setColumns("nome");

        List<Material> materiais = materialRepository.pesquisarTodos();
        grid.setItems(materiais);

        Editor<Material> editor = grid.getEditor();
        Binder<Material> binder = new Binder<>(Material.class);
        editor.setBinder(binder);

        TextField nomeEditor = new TextField();
        binder.forField(nomeEditor).bind(Material::getNome, Material::setNome);
        grid.getColumnByKey("nome").setEditorComponent(nomeEditor);

        grid.addItemDoubleClickListener(event -> editor.editItem(event.getItem()));
        editor.addCloseListener(event -> grid.getDataProvider().refreshItem(event.getItem()));
        
        grid.addComponentColumn(material -> {
            Button alterarButton = new Button("Alterar", new Icon(VaadinIcon.COG));
            alterarButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.save();
                    Material editedMaterial = editor.getItem();
                    if (materialRepository.alterar(editedMaterial)) {
                        Notification notification = new Notification(
                                "Material atualizado com sucesso.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                        
                        materiais.clear();
                        materiais.addAll(materialRepository.pesquisarTodos());
                        grid.getDataProvider().refreshAll();
                    } else {
                        Notification notification = new Notification(
                                "Erro ao atualizar. Verifique se todos os dados foram preenchidos.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                    }
                } else {
                    editor.editItem(material);
                    nomeEditor.focus();
                }
            });
            return alterarButton;
        }).setHeader("Alterar");
        
        editor.addSaveListener(event -> {
            grid.getDataProvider().refreshItem(event.getItem());
        });

        grid.addComponentColumn(material -> {
            Button deletarButton = new Button(new Icon(VaadinIcon.TRASH));
            deletarButton.addClickListener(e -> {
                if (materialRepository.excluir(material)) {
                    Notification notification = new Notification(
                            "Material deletado com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                    
                    materiais.clear();
                    materiais.addAll(materialRepository.pesquisarTodos());
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification notification = new Notification(
                            "Erro ao deletar. Tente novamente.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
            return deletarButton;
        }).setHeader("Deletar");
    

        Button confirmarButton = new Button("Salvar", event -> {
            if(nomeField.isEmpty()){
                Notification notification = new Notification(
                    "Erro: O nome não pode estar vazio.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            } else {
                Material material = new Material();
                material.setNome(nomeField.getValue());
                if (materialRepository.inserir(material) == true) {
                    Notification notification = new Notification(
                            "Material salvo com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    nomeField.clear();
                    materiais.clear();
                    materiais.addAll(materialRepository.pesquisarTodos());
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification notification = new Notification(
                            "Erro ao salvar. Verifique se todos os dados foram preenchidos.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            }
        });
        Button cancelarButton = new Button("Fechar", event -> dialog.close());

        cancelarButton.getStyle()
            .set("background-color", "#FF0000")
            .set("color", "#FFFFFF")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.2)")
            .set("cursor", "pointer");

        confirmarButton.getStyle()
            .set("background-color", "#228B22")
            .set("color", "#FFFFFF")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.2)")
            .set("cursor", "pointer");

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelarButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setPadding(false);
        buttonLayout.setSpacing(true);

        formLayout.add(nomeField, confirmarButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, grid, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openDialog3() {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");

        Grid<UnidMedida> grid = new Grid<>(UnidMedida.class);
        grid.setColumns("nome");

        List<UnidMedida> medidas = unidMedidaRepository.pesquisarTodos();
        grid.setItems(medidas);

        Editor<UnidMedida> editor = grid.getEditor();
        Binder<UnidMedida> binder = new Binder<>(UnidMedida.class);
        editor.setBinder(binder);

        TextField nomeEditor = new TextField();
        binder.forField(nomeEditor).bind(UnidMedida::getNome, UnidMedida::setNome);
        grid.getColumnByKey("nome").setEditorComponent(nomeEditor);

        grid.addItemDoubleClickListener(event -> editor.editItem(event.getItem()));
        editor.addCloseListener(event -> grid.getDataProvider().refreshItem(event.getItem()));
        
        grid.addComponentColumn(unidMedida -> {
            Button alterarButton = new Button("Alterar", new Icon(VaadinIcon.COG));
            alterarButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.save();
                    UnidMedida editedUnidMedida = editor.getItem();
                    if (unidMedidaRepository.alterar(editedUnidMedida)) {
                        Notification notification = new Notification(
                                "Unidade de Medida atualizada com sucesso.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                        
                        medidas.clear();
                        medidas.addAll(unidMedidaRepository.pesquisarTodos());
                        grid.getDataProvider().refreshAll();
                    } else {
                        Notification notification = new Notification(
                                "Erro ao atualizar. Verifique se todos os dados foram preenchidos.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                    }
                } else {
                    editor.editItem(unidMedida);
                    nomeEditor.focus();
                }
            });
            return alterarButton;
        }).setHeader("Alterar");
        
        editor.addSaveListener(event -> {
            grid.getDataProvider().refreshItem(event.getItem());
        });

        grid.addComponentColumn(unidMedida -> {
            Button deletarButton = new Button(new Icon(VaadinIcon.TRASH));
            deletarButton.addClickListener(e -> {
                if (unidMedidaRepository.excluir(unidMedida)) {
                    Notification notification = new Notification(
                            "Unidade de medida deletada com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                    
                    medidas.clear();
                    medidas.addAll(unidMedidaRepository.pesquisarTodos());
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification notification = new Notification(
                            "Erro ao deletar. Tente novamente.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            });
            return deletarButton;
        }).setHeader("Deletar");
    

        Button confirmarButton = new Button("Salvar", event -> {
            if(nomeField.isEmpty()){
                Notification notification = new Notification(
                    "Erro: O nome não pode estar vazio.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            } else {
                UnidMedida unidMedida = new UnidMedida();
                unidMedida.setNome(nomeField.getValue());
                if (unidMedidaRepository.inserir(unidMedida) == true) {
                    Notification notification = new Notification(
                            "Unidade de medida salva com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    nomeField.clear();
                    medidas.clear();
                    medidas.addAll(unidMedidaRepository.pesquisarTodos());
                    grid.getDataProvider().refreshAll();
                } else {
                    Notification notification = new Notification(
                            "Erro ao salvar. Verifique se todos os dados foram preenchidos.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();
                }
            }
        });
        Button cancelarButton = new Button("Fechar", event -> dialog.close());

        cancelarButton.getStyle()
            .set("background-color", "#FF0000")
            .set("color", "#FFFFFF")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.2)")
            .set("cursor", "pointer");

        confirmarButton.getStyle()
            .set("background-color", "#228B22")
            .set("color", "#FFFFFF")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.2)")
            .set("cursor", "pointer");

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelarButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setPadding(false);
        buttonLayout.setSpacing(true);

        formLayout.add(nomeField, confirmarButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, grid, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

}

