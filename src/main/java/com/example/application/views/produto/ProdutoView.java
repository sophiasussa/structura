package com.example.application.views.produto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.example.application.model.Material;
import com.example.application.model.Modelo;
import com.example.application.model.Produto;
import com.example.application.model.UnidMedida;
import com.example.application.repository.DaoMaterial;
import com.example.application.repository.DaoModelo;
import com.example.application.repository.DaoProduto;
import com.example.application.repository.DaoUnidMedida;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Produtos")
@Route(value = "my-view4", layout = MainLayout.class)
public class ProdutoView extends VerticalLayout {

    private DaoProduto produtoRepository = new DaoProduto();
    private DaoMaterial materialRepository = new DaoMaterial();
    private DaoUnidMedida unidMedidaRepository = new DaoUnidMedida();
    private DaoModelo modeloRepository = new DaoModelo();
    private Grid<Produto> grid = new Grid<>(Produto.class, false);
    private TextField nome = new TextField("Nome");
    private TextField quantidadeAtual = new TextField("Quantidade Atual");
    private TextField quantidadeMinima = new TextField("Quantidade Mínima");
    private TextField custoUnitario = new TextField("Custo Unitário");
    private Button buttonTertiary = new Button();
    private Button buttonTertiary2 = new Button();
    private Button buttonTertiary3 = new Button();
    private ComboBox<Material> material = new ComboBox<>("Material");
    private ComboBox<UnidMedida> unidMedida = new ComboBox<>("Unidade de Medida");
    private ComboBox<Modelo> modelo = new ComboBox<>("Modelo");
    private Long produtoId;
    private TabSheet tabSheet;

    public ProdutoView() {
        tabSheet = new TabSheet();
        this.setWidth("100%");
        this.getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        this.add(tabSheet);
    }

    // This method sets up two tabs
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div produtosContent = createProdutosContent();
        tabSheet.add("Produtos", produtosContent);

        Div addProdutosContent = createAddProdutosContent();
        tabSheet.add("Adicionar Produto", addProdutosContent);
    }

    // This method creates the content for the "Produtos" tab, which consists of a
    // form to see all the Produtos and search for a specific produto
    private Div createProdutosContent() {
        Div produtosContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Produto> resultados;

            if (pesquisa.isEmpty()) {
                resultados = produtoRepository.pesquisarTodos();
            } else {
                resultados = produtoRepository.pesquisarProduto(pesquisa);
            }

            if (resultados.isEmpty()) {
                Notification.show("Nenhum resultado encontrado para: " + pesquisa);
            }

            grid.setItems(resultados);
        });

        // For a better interface
        textField.setPlaceholder("Nome ou Material");
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

        grid = createGrid();

        layout.add(layoutRow, space, grid);
        produtosContentDiv.add(layout);

        return produtosContentDiv;
    }

    // This method creates the content for the "Adicionar Produto" tab, which
    // consists of a form to add a new Produto.
    private Div createAddProdutosContent() {
        Div addProdutosContentDiv = new Div();
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
        quantidadeAtual = new TextField("Quantidade Atual");
        quantidadeMinima = new TextField("Quantidade Mínima");
        custoUnitario = new TextField("Custo Unitário");

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
        setComboBoxModeloData(modelo);
        modelo.setWidth("min-content");
        buttonTertiary.setText("+ Modelo");
        buttonTertiary.setWidth("min-content");
        buttonTertiary.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonTertiary.addClickListener(event -> openDialog());

        Button saveButton = new Button("Salvar", event -> {
            if (nome.isEmpty()) {
                Notification.show("Preencha o campo obrigatório: Nome", 3000, Notification.Position.MIDDLE);
                return;
            }
            String nomeProduto = nome.getValue();
            Integer quantidadeAtualProduto = quantidadeAtual.isEmpty() ? null : Integer.valueOf(quantidadeAtual.getValue());
            Integer quantidadeMinimaProduto = quantidadeMinima.isEmpty() ? null : Integer.valueOf(quantidadeMinima.getValue());
            Material materialProduto = material.isEmpty() ? null : material.getValue();
            UnidMedida unidMedidaProduto = unidMedida.isEmpty() ? null : unidMedida.getValue();
            Modelo modeloProduto = modelo.isEmpty() ? null : modelo.getValue();
            Double custoUnitarioProduto = custoUnitario.isEmpty() ? 0.0 : Double.parseDouble(custoUnitario.getValue());

            Produto produto = new Produto(nomeProduto, quantidadeAtualProduto, quantidadeMinimaProduto,
                    custoUnitarioProduto, materialProduto, unidMedidaProduto, modeloProduto);
            produto.setId(produtoId);

            boolean sucesso;
            if (produtoId != null && produtoId > 0) {
                sucesso = produtoRepository.alterar(produto);
                if (sucesso) {
                    Notification.show("Produto atualizado com sucesso!");
                } else {
                    Notification.show("Erro ao atualizar o produto", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = produtoRepository.inserir(produto);
                if (sucesso) {
                    Notification.show("Produto salvo com sucesso!");
                } else {
                    Notification.show("Erro ao salvar o produto", 3000, Notification.Position.MIDDLE);
                }
            }

            if (sucesso) {
                clearForm();
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        // For a better interface
        nome.addClassName("rounded-text-field");
        quantidadeAtual.addClassName("rounded-text-field");
        quantidadeMinima.addClassName("rounded-text-field");
        custoUnitario.addClassName("rounded-text-field");
        custoUnitario.setPlaceholder("Exemplo: 1000.00 ou 1000");
        material.addClassName("rounded-text-field");
        unidMedida.addClassName("rounded-text-field");
        modelo.addClassName("rounded-text-field");
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
                new FormLayout.ResponsiveStep("500px", 3));

        formLayout2Col.add(nome, custoUnitario, quantidadeAtual, quantidadeMinima);
        formLayout3Col.add(material, unidMedida, modelo, buttonTertiary2, buttonTertiary3, buttonTertiary);
        layout2.add(formLayout2Col, formLayout3Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addProdutosContentDiv.add(space1, layout);

        return addProdutosContentDiv;
    }

    private Grid<Produto> createGrid() {
        grid = new Grid<>(Produto.class, false);
        grid.addClassName("borderless-grid");
        grid.setAllRowsVisible(true);

        grid.addColumn(Produto::getNome).setHeader("Nome").setSortable(true);
        grid.addColumn(produto -> produto.getMaterial() != null && produto.getMaterial().getNome() != null
                ? produto.getMaterial().getNome()
                : "Sem Material").setHeader("Material").setSortable(true);

        grid.addColumn(produto -> produto.getModelo() != null && produto.getModelo().getNome() != null
                ? produto.getModelo().getNome()
                : "Sem Modelo").setHeader("Modelo").setSortable(true);

        grid.addComponentColumn(produto -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir essa produto " + produto.getNome() + "?"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deleteProduto(produto);
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
        grid.setItemDetailsRenderer(createProdutoDetailsRenderer());

        grid.addItemClickListener(event -> {
            Produto produto = event.getItem();
            grid.setDetailsVisible(produto, !grid.isDetailsVisible(produto));
        });

        grid.addItemDoubleClickListener(event -> {
            Produto produto = event.getItem();
            editProduto(produto);
            tabSheet.setSelectedIndex(1);
        });

        grid.setItems(produtoRepository.pesquisarTodos());

        return grid;
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

    private void setComboBoxModeloData(ComboBox<Modelo> comboBox) {
        List<Modelo> modelos = modeloRepository.pesquisarTodos();
        comboBox.setItems(modelos);
        comboBox.setItemLabelGenerator(modelo -> modelo.getNome());
    }

    private void refreshGrid() {
        List<Produto> produtos = produtoRepository.pesquisarTodos();
        grid.setItems(produtos);
    }

    private void deleteProduto(Produto produto) {
        boolean success = produtoRepository.excluir(produto);
        if (success) {
            refreshGrid();
        } else {
            System.out.println("Erro ao excluir produto");
        }
    }

    private void editProduto(Produto produto) {
        produtoId = produto.getId();
        nome.setValue(produto.getNome());
        quantidadeAtual.setValue(produto.getQuantidadeAtual() != null ? String.valueOf(produto.getQuantidadeAtual()) : "");
        quantidadeMinima.setValue(produto.getQuantidadeMinima() != null ? String.valueOf(produto.getQuantidadeMinima()) : "");
        custoUnitario.setValue(produto.getCustoUnitario() != null ? produto.getCustoUnitario().toString() : "");
        material.setValue(produto.getMaterial());
        unidMedida.setValue(produto.getUnidMedida());
        modelo.setValue(produto.getModelo());
    }

    private void clearForm() {
        produtoId = null;
        nome.clear();
        quantidadeAtual.clear();
        quantidadeMinima.clear();
        custoUnitario.clear();
        material.clear();
        unidMedida.clear();
        modelo.clear();
    }

    private static ComponentRenderer<HorizontalLayout, Produto> createProdutoDetailsRenderer() {
        return new ComponentRenderer<>(produto -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setSpacing(true);
            detailsLayout.setPadding(true);
            detailsLayout.addClassName("details-layout");

            TextField unidMedidaField = new TextField("Unidade de Medida");
            String unidMedidaValue = produto.getUnidMedida() != null && produto.getUnidMedida().getNome() != null
                    ? produto.getUnidMedida().getNome()
                    : "Sem Unidade de Medida";
            unidMedidaField.setValue(unidMedidaValue);
            unidMedidaField.setReadOnly(true);
            unidMedidaField.addClassName("rounded-text-field");

            TextField quantidadeAtualField = new TextField("Quantidade Atual");
            quantidadeAtualField.setValue(String.valueOf(produto.getQuantidadeAtual()));
            quantidadeAtualField.setReadOnly(true);
            quantidadeAtualField.addClassName("rounded-text-field");

            TextField quantidadeMinimaField = new TextField("Quantidade Mínima");
            quantidadeMinimaField.setValue(String.valueOf(produto.getQuantidadeMinima()));
            quantidadeMinimaField.setReadOnly(true);
            quantidadeMinimaField.addClassName("rounded-text-field");

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String custoFormatado = currencyFormat.format(produto.getCustoUnitario());

            TextField custoUnitarioField = new TextField("Custo Unitário");
            custoUnitarioField.setValue(custoFormatado);
            custoUnitarioField.setReadOnly(true);
            custoUnitarioField.addClassName("rounded-text-field");

            detailsLayout.add(unidMedidaField, quantidadeAtualField, quantidadeMinimaField, custoUnitarioField);

            return detailsLayout;
        });
    }

    private void openDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        FormLayout formLayout = new FormLayout();
        TextField nomeField = new TextField("Nome");

        Grid<Modelo> grid = new Grid<>(Modelo.class);
        grid.setColumns("nome");

        List<Modelo> modelos = modeloRepository.pesquisarTodos();
        grid.setItems(modelos);

        Editor<Modelo> editor = grid.getEditor();
        Binder<Modelo> binder = new Binder<>(Modelo.class);
        editor.setBinder(binder);

        TextField nomeEditor = new TextField();
        binder.forField(nomeEditor).bind(Modelo::getNome, Modelo::setNome);
        grid.getColumnByKey("nome").setEditorComponent(nomeEditor);

        grid.addItemDoubleClickListener(event -> editor.editItem(event.getItem()));
        editor.addCloseListener(event -> grid.getDataProvider().refreshItem(event.getItem()));

        grid.addComponentColumn(modelo -> {
            Button alterarButton = new Button("Alterar", new Icon(VaadinIcon.COG));
            editor.addOpenListener(event -> {
                if (event.getItem() == modelo) {
                    alterarButton.setText("Salvar Alteração");
                }
            });
            alterarButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.save();
                    Modelo editedModelo = editor.getItem();
                    if (modeloRepository.alterar(editedModelo)) {
                        Notification notification = new Notification(
                                "Modelo atualizado com sucesso.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();

                        modelos.clear();
                        modelos.addAll(modeloRepository.pesquisarTodos());
                        grid.getDataProvider().refreshAll();
                    } else {
                        Notification notification = new Notification(
                                "Erro ao atualizar. Verifique se todos os dados foram preenchidos.", 3000);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.MIDDLE);
                        notification.open();
                    }
                } else {
                    editor.editItem(modelo);
                    nomeEditor.focus();
                }
            });
            return alterarButton;
        }).setHeader("Alterar");

        editor.addSaveListener(event -> {
            grid.getDataProvider().refreshItem(event.getItem());
        });

        grid.addComponentColumn(modelo -> {
            Button deletarButton = new Button(new Icon(VaadinIcon.TRASH));
            deletarButton.addClickListener(e -> {
                if (modeloRepository.excluir(modelo)) {
                    Notification notification = new Notification(
                            "Modelo deletado com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    modelos.clear();
                    modelos.addAll(modeloRepository.pesquisarTodos());
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
            if (nomeField.isEmpty()) {
                Notification notification = new Notification(
                        "Erro: O nome não pode estar vazio.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            } else {
                Modelo modelo = new Modelo();
                modelo.setNome(nomeField.getValue());
                if (modeloRepository.inserir(modelo) == true) {
                    Notification notification = new Notification(
                            "Modelo salvo com sucesso.", 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    nomeField.clear();
                    modelos.clear();
                    modelos.addAll(modeloRepository.pesquisarTodos());
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
        Button cancelarButton = new Button("Fechar", event -> {
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

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
            editor.addOpenListener(event -> {
                if (event.getItem() == material) {
                    alterarButton.setText("Salvar Alteração");
                }
            });
            alterarButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    alterarButton.setText("Salvar");
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
            if (nomeField.isEmpty()) {
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
        Button cancelarButton = new Button("Fechar", event -> {
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

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
            editor.addOpenListener(event -> {
                if (event.getItem() == unidMedida) {
                    alterarButton.setText("Salvar Alteração");
                }
            });
            alterarButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    alterarButton.setText("Salvar");
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
            if (nomeField.isEmpty()) {
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
        Button cancelarButton = new Button("Fechar", event -> {
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

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
