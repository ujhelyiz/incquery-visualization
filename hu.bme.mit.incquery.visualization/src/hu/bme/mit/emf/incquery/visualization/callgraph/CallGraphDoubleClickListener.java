package hu.bme.mit.emf.incquery.visualization.callgraph;

import hu.bme.mit.emf.incquery.visualization.model.PatternElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.incquery.patternlanguage.patternLanguage.Pattern;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.util.ITextRegion;

public class CallGraphDoubleClickListener implements IDoubleClickListener {

    private ILocationInFileProvider locationProvider;

    public CallGraphDoubleClickListener(ILocationInFileProvider location) {
        locationProvider = location;
    }

    @Override
    public void doubleClick(DoubleClickEvent event) {
        final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        StructuredSelection selection = (StructuredSelection) event.getSelection();
        Object o = selection.getFirstElement();
        if (o instanceof PatternElement) {
            Pattern pattern = ((PatternElement) o).getPattern();
            URI uri = pattern.eResource().getURI();
            IFile file = workspaceRoot.getFile(new Path(uri.toPlatformString(true)));

            for (IEditorReference ref : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getEditorReferences()) {
                String id = ref.getId();
                IEditorPart editor = ref.getEditor(true);
                if (id.equals("org.eclipse.incquery.patternlanguage.emf.EMFPatternLanguage")) {
                    // The editor id always registers an Xtext editor
                    assert editor instanceof XtextEditor;
                    XtextEditor providerEditor = (XtextEditor) editor;
                    // Bringing editor to top
                    IEditorInput input = providerEditor.getEditorInput();
                    if (input instanceof FileEditorInput) {
                        FileEditorInput editorInput = (FileEditorInput) input;
                        if (editorInput.getFile().equals(file)) {
                            editor.getSite().getPage().bringToTop(editor);
                        }
                    }
                    // Finding location using location service
                    ITextRegion location = locationProvider.getSignificantTextRegion(pattern);
                    // Location can be null in case of error
                    if (location != null) {
                        providerEditor.reveal(location.getOffset(), location.getLength());
                        providerEditor.getSelectionProvider().setSelection(
                                new TextSelection(location.getOffset(), location.getLength()));
                    }
                }
            }
        }

    }

}
