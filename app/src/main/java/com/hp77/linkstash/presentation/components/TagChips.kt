package com.hp77.linkstash.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hp77.linkstash.domain.model.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagChips(
    tags: List<Tag>,
    selectedTags: List<Tag> = emptyList(),
    onTagClick: (Tag) -> Unit = {},
    onTagRemove: ((Tag) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        tags.forEach { tag ->
            val isSelected = tag in selectedTags
            val tagColor = tag.color?.let { Color(android.graphics.Color.parseColor(it)) }
                ?: MaterialTheme.colorScheme.primary

            if (onTagRemove != null) {
                AssistChip(
                    onClick = { onTagClick(tag) },
                    label = { Text(tag.name) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove ${tag.name}",
                            tint = tagColor
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                ElevatedAssistChip(
                    onClick = { onTagClick(tag) },
                    label = { Text(tag.name) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SelectedTagChips(
    selectedTags: List<Tag>,
    onTagRemove: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    TagChips(
        tags = selectedTags,
        onTagClick = onTagRemove,
        onTagRemove = onTagRemove,
        modifier = modifier
    )
}