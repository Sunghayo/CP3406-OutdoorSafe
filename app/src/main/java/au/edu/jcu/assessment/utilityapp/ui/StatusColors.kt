package au.edu.jcu.assessment.utilityapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class StatusColors(val background: Color, val foreground: Color)

@Composable
fun statusColors(severity: Severity): StatusColors {
    val dark = isSystemInDarkTheme()
    return if (dark) when (severity) {
        Severity.GOOD -> StatusColors(Color(0xFF173C2A), Color(0xFFBBF7D0))
        Severity.FAIR -> StatusColors(Color(0xFF134E4A), Color(0xFFCCFBF1))
        Severity.MODERATE -> StatusColors(Color(0xFF49350E), Color(0xFFFDE68A))
        Severity.HIGH -> StatusColors(Color(0xFF5A2916), Color(0xFFFED7AA))
        Severity.VERY_HIGH -> StatusColors(Color(0xFF581C1C), Color(0xFFFECACA))
        Severity.EXTREME -> StatusColors(Color(0xFF3B1B56), Color(0xFFE9D5FF))
        Severity.UNKNOWN -> StatusColors(Color(0xFF334155), Color(0xFFE2E8F0))
    } else when (severity) {
        Severity.GOOD -> StatusColors(Color(0xFFDCFCE7), Color(0xFF14532D))
        Severity.FAIR -> StatusColors(Color(0xFFCCFBF1), Color(0xFF134E4A))
        Severity.MODERATE -> StatusColors(Color(0xFFFEF3C7), Color(0xFF713F12))
        Severity.HIGH -> StatusColors(Color(0xFFFFEDD5), Color(0xFF9A3412))
        Severity.VERY_HIGH -> StatusColors(Color(0xFFFEE2E2), Color(0xFF991B1B))
        Severity.EXTREME -> StatusColors(Color(0xFFF3E8FF), Color(0xFF581C87))
        Severity.UNKNOWN -> StatusColors(Color(0xFFE2E8F0), Color(0xFF334155))
    }
}
