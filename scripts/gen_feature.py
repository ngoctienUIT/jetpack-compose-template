#!/usr/bin/env python3

import os
import sys
from pathlib import Path

def get_project_root():
    return Path(__file__).resolve().parents[1]

def find_base_package_info(root):
    # Find TemplateApplication.kt to determine the base package and path
    app_java_root = root / "app" / "src" / "main" / "java"
    for path in app_java_root.rglob("TemplateApplication.kt"):
        relative_path = path.parent.relative_to(app_java_root)
        package_name = ".".join(relative_path.parts)
        return package_name, path.parent

    # Fallback to default
    return "com.ngoctientnt.template", app_java_root / "com" / "ngoctientnt" / "template"

import re

def to_pascal_case(name):
    # Split by underscores, spaces, or camelCase boundaries
    s1 = re.sub(r'([a-z])([A-Z])', r'\1 \2', name)
    words = re.split(r'[_ ]', s1)
    return "".join(word.capitalize() for word in words if word)

def to_camel_case(name):
    pascal = to_pascal_case(name)
    return pascal[0].lower() + pascal[1:]

def generate_feature(name):
    root = get_project_root()
    base_package, base_path = find_base_package_info(root)

    feature_name = to_pascal_case(name)
    feature_camel = to_camel_case(name)
    feature_pkg = f"{base_package}.feature.{feature_name.lower()}"
    feature_dir = base_path / "feature" / feature_name.lower()

    if feature_dir.exists():
        print(f"Error: Feature '{feature_name}' already exists at {feature_dir}")
        sys.exit(1)

    os.makedirs(feature_dir, exist_ok=True)

    # 1. Generate Contract
    contract_content = f"""package {feature_pkg}

data class {feature_name}UiState(
    val isLoading: Boolean = false,
)

sealed interface {feature_name}Intent {{
    data object Refresh : {feature_name}Intent
}}

sealed interface {feature_name}Effect {{
    data class ShowToast(val message: String) : {feature_name}Effect
}}
"""
    (feature_dir / f"{feature_name}Contract.kt").write_text(contract_content)

    # 2. Generate ViewModel
    vm_content = f"""package {feature_pkg}

import androidx.lifecycle.viewModelScope
import {base_package}.core.architecture.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class {feature_name}ViewModel @Inject constructor() :
    BaseViewModel<{feature_name}UiState, {feature_name}Intent, {feature_name}Effect>({feature_name}UiState()) {{

    override fun onIntent(intent: {feature_name}Intent) {{
        when (intent) {{
            {feature_name}Intent.Refresh -> refresh()
        }}
    }}

    private fun refresh() {{
        viewModelScope.launch {{
            reduce {{ copy(isLoading = true) }}
            // TODO: Implementation
            reduce {{ copy(isLoading = false) }}
        }}
    }}
}}
"""
    (feature_dir / f"{feature_name}ViewModel.kt").write_text(vm_content)

    # 3. Generate Screen
    screen_content = f"""package {feature_pkg}

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun {feature_name}Screen(
    viewModel: {feature_name}ViewModel = hiltViewModel(),
) {{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {{
        viewModel.effect.collect {{ effect ->
            when (effect) {{
                is {feature_name}Effect.ShowToast -> {{
                    // TODO: Handle effect
                }}
            }}
        }}
    }}

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {{
        if (uiState.isLoading) {{
            CircularProgressIndicator()
        }} else {{
            Text(text = "{feature_name} Screen")
        }}
    }}
}}
"""
    (feature_dir / f"{feature_name}Screen.kt").write_text(screen_content)

    print(f"Successfully generated feature '{feature_name}' in {feature_dir}")
    print("\nNext steps:")
    print(f"1. Add {feature_name}Route to app/src/main/java/{base_package.replace('.', '/')}/app/navigation/Route.kt")
    print(f"2. Add entry<{feature_name}Route> to app/src/main/java/{base_package.replace('.', '/')}/app/navigation/AppNavHost.kt")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 scripts/gen_feature.py <FeatureName>")
        sys.exit(1)

    generate_feature(sys.argv[1])
