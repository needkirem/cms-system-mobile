package dev.needkirem.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.fastadapter.IItem
import dev.needkirem.sample.data.repository.CmsPageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val repository = CmsPageRepository()
    private val factory = WidgetItemFactory()

    private val _widgets: MutableStateFlow<List<IItem<*>>> = MutableStateFlow(emptyList())
    val widgets = _widgets.asStateFlow()

    fun init() {
        loadWidgets()
    }

    private fun loadWidgets() {
        viewModelScope.launch {
            val cmsPage = repository.getCmsPage()
            val widgetItems = cmsPage.widgets.mapNotNull {
                factory.create(it)
            }
            _widgets.emit(widgetItems)
        }
    }
}