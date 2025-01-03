package com.hp77.linkstash.presentation.home

import com.hp77.linkstash.domain.model.Link
import com.hp77.linkstash.domain.model.LinkFilter
import com.hp77.linkstash.data.preferences.ThemeMode

sealed class HomeScreenEvent {
    data class OnSearchQueryChange(val query: String) : HomeScreenEvent()
    data class OnToggleFavorite(val link: Link) : HomeScreenEvent()
    data class OnToggleArchive(val link: Link) : HomeScreenEvent()
    data class OnFilterSelect(val filter: LinkFilter) : HomeScreenEvent()
    data class OnThemeSelect(val theme: ThemeMode) : HomeScreenEvent()
    data class OnDeleteLink(val link: Link) : HomeScreenEvent()
    object OnErrorDismiss : HomeScreenEvent()
    object OnDrawerOpen : HomeScreenEvent()
    object OnDrawerClose : HomeScreenEvent()
    object OnProfileClick : HomeScreenEvent()
    object OnProfileDismiss : HomeScreenEvent()
    data class OnToggleStatus(val link: Link) : HomeScreenEvent()
    data class OnShowShareSheet(val link: Link) : HomeScreenEvent()
    object OnDismissShareSheet : HomeScreenEvent()
    data class OnShareToHackerNews(val link: Link) : HomeScreenEvent()
    data class OnSyncToGitHub(val link: Link) : HomeScreenEvent()
    object NavigateToSettings : HomeScreenEvent()
    object ClearGitHubProfile : HomeScreenEvent()
    object ClearHackerNewsProfile : HomeScreenEvent()
    
    // Issue Reporting Events
    object ShowIssueReport : HomeScreenEvent()
    object HideIssueReport : HomeScreenEvent()
    data class UpdateIssueDescription(val description: String) : HomeScreenEvent()
    data class ReportIssue(val description: String) : HomeScreenEvent()
}
