package com.darkndev.everkeepcompose.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkndev.everkeepcompose.data.LabelDao
import com.darkndev.everkeepcompose.models.Label
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val labelDao: LabelDao
) : ViewModel() {

    val allLabels = labelDao.getAllLabels()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addLabel(text: String) = viewModelScope.launch {
        labelDao.upsertLabel(Label(label = text))
    }

    fun deleteLabel(label: Label) = viewModelScope.launch {
        labelDao.deleteLabel(label)
    }

}