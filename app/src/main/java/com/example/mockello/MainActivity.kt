package com.example.mockello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.mockello.ui.theme.MockelloTheme
import com.example.mockello.ui.theme.MockelloOrange
import com.example.mockello.ui.theme.MockelloOrangeLight
import com.example.mockello.ui.theme.MockelloDark

// Navigation screens
enum class Screen {
    HOME, CLIENT_PORTAL, EMPLOYER_PORTAL, PARTNER_PORTAL, CLIENT_LOGIN, CLIENT_DASHBOARD
}

// Data classes for client portal
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val status: ProjectStatus,
    val progress: Float,
    val startDate: String,
    val endDate: String?,
    val technologies: List<String>
)

enum class ProjectStatus {
    UPCOMING, ONGOING, COMPLETED
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MockelloTheme {
                MockelloApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockelloApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(
                onNavigateToPortal = { screen ->
                    currentScreen = screen
                    scope.launch { drawerState.close() }
                },
                onNavigateHome = {
                    currentScreen = Screen.HOME
                    isLoggedIn = false
                    currentUser = ""
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (currentScreen != Screen.HOME) {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (currentScreen) {
                                    Screen.CLIENT_PORTAL -> "Client Portal"
                                    Screen.CLIENT_LOGIN -> "Client Login"
                                    Screen.CLIENT_DASHBOARD -> "Dashboard - $currentUser"
                                    Screen.EMPLOYER_PORTAL -> "Employer Portal"
                                    Screen.PARTNER_PORTAL -> "Partner Portal"
                                    else -> "Mockello"
                                },
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (currentScreen == Screen.CLIENT_DASHBOARD || 
                                        currentScreen == Screen.CLIENT_LOGIN) {
                                        currentScreen = if (isLoggedIn) Screen.CLIENT_DASHBOARD else Screen.CLIENT_PORTAL
                                    } else {
                                        currentScreen = Screen.HOME
                                        isLoggedIn = false
                                        currentUser = ""
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MockelloOrange
                        )
                    )
                }
            }
        ) { paddingValues ->
            when (currentScreen) {
                Screen.HOME -> LandingPage(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier.padding(paddingValues)
                )
                Screen.CLIENT_PORTAL -> ClientPortalScreen(
                    onNavigateToLogin = { currentScreen = Screen.CLIENT_LOGIN },
                    modifier = Modifier.padding(paddingValues)
                )
                Screen.CLIENT_LOGIN -> ClientLoginScreen(
                    onLoginSuccess = { username ->
                        currentUser = username
                        isLoggedIn = true
                        currentScreen = Screen.CLIENT_DASHBOARD
                    },
                    modifier = Modifier.padding(paddingValues)
                )
                Screen.CLIENT_DASHBOARD -> ClientDashboardScreen(
                    username = currentUser,
                    modifier = Modifier.padding(paddingValues)
                )
                Screen.EMPLOYER_PORTAL -> ComingSoonScreen(
                    title = "Employer Portal",
                    modifier = Modifier.padding(paddingValues)
                )
                Screen.PARTNER_PORTAL -> ComingSoonScreen(
                    title = "Partner Portal", 
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun LandingPage(
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    // Floating elements animation
    val floatingOffset by animateFloatAsState(
        targetValue = if (scrollState.value > 100) -20f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "floating"
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Animated background with parallax
        AnimatedBackground(scrollState)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AnimatedHeroSection()
            AnimatedSpacer(60.dp)
            AnimatedAboutSection()
            AnimatedSpacer(80.dp)
            AnimatedResearchSection()
            AnimatedSpacer(80.dp)
            ModernTechStackSection()
            AnimatedSpacer(80.dp)
            StunningPartnershipSection()
            AnimatedSpacer(80.dp)
            ModernCTASection()
            AnimatedSpacer(60.dp)
        }
        
        // Floating action elements
        FloatingElements(
            onMenuClick = onMenuClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = floatingOffset.dp, y = floatingOffset.dp)
        )
    }
}

@Composable
fun AnimatedBackground(scrollState: ScrollState) {
    val parallaxOffset = scrollState.value * 0.5f
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFF8F5),
                        Color(0xFFFFE0B2),
                        MockelloOrangeLight.copy(alpha = 0.2f),
                        Color(0xFFFFF8F5)
                    ),
                    radius = 1200f + parallaxOffset
                )
            )
    )
    
    // Animated floating circles
    repeat(5) { index ->
        val animatedOffset by animateFloatAsState(
            targetValue = (scrollState.value * (0.1f + index * 0.05f)),
            animationSpec = tween(1000),
            label = "circle$index"
        )
        
        Box(
            modifier = Modifier
                .offset(
                    x = (100 + index * 150).dp,
                    y = (200 + index * 180 - animatedOffset).dp
                )
                .size((60 + index * 20).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MockelloOrangeLight.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun AnimatedSpacer(height: Dp) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(600)),
        exit = fadeOut()
    ) {
        Spacer(modifier = Modifier.height(height))
    }
}

@Composable
fun AnimatedHeroSection() {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(1000)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Modern gradient tagline banner
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MockelloOrange.copy(alpha = 0.9f),
                                MockelloOrangeLight.copy(alpha = 0.8f),
                                Color(0xFFFF7043).copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .animateContentSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        
                        Text(
                            text = "AI + FULL-STACK",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp
                            ),
                            color = Color.White
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = "DEVELOPMENT COMPANY",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        ),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Animated main headline with typewriter effect
            TypewriterText(
                text = "We Are\nMockello",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                textAlign = TextAlign.Center,
                color = MockelloDark,
                lineHeight = 64.sp
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Animated subheadline
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 1500))
            ) {
                Text(
                    text = "Pioneering AI Research &\nFull-Stack Innovation",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    color = MockelloOrange,
                    lineHeight = 32.sp
                )
            }
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // Animated description
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 2000))
            ) {
                Text(
                    text = "Focused heavily on AI and full-stack development — from building custom AI agents and training models, to mobile app development, backend/frontend systems, and cloud infrastructure.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MockelloDark.copy(alpha = 0.8f),
                    lineHeight = 26.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Animated CTA Buttons
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(1000, delayMillis = 2500)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedButton(
                        text = "Explore Our Work",
                        icon = Icons.Default.Build,
                        isPrimary = true,
                        onClick = { /* TODO */ }
                    )
                    
                    AnimatedButton(
                        text = "Contact Us",
                        icon = Icons.Default.Email,
                        isPrimary = false,
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}

@Composable
fun GlassMorphismCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.1f)
                    )
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MockelloOrangeLight.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun TypewriterText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    textAlign: TextAlign,
    color: Color,
    lineHeight: androidx.compose.ui.unit.TextUnit
) {
    var displayedText by remember { mutableStateOf("") }
    
    LaunchedEffect(text) {
        displayedText = ""
        text.forEachIndexed { index, _ ->
            delay(100)
            displayedText = text.substring(0, index + 1)
        }
    }
    
    Text(
        text = displayedText,
        style = style,
        textAlign = textAlign,
        color = color,
        lineHeight = lineHeight
    )
}

@Composable
fun AnimatedButton(
    text: String,
    icon: ImageVector,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )
    
    if (isPrimary) {
        Button(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .height(56.dp)
                .scale(scale)
                .clip(RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MockelloOrange
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .height(56.dp)
                .scale(scale)
                .clip(RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MockelloOrange
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(
                    colors = listOf(MockelloOrange, MockelloOrangeLight)
                )
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun FloatingElements(
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var extended by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (extended) 180f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "rotation"
    )
    
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Additional action buttons when extended
        AnimatedVisibility(
            visible = extended,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(48.dp),
                    containerColor = Color.White,
                    contentColor = MockelloOrange
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                
                FloatingActionButton(
                    onClick = { /* Share action */ },
                    modifier = Modifier.size(48.dp),
                    containerColor = Color.White,
                    contentColor = MockelloOrange
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                
                FloatingActionButton(
                    onClick = { /* Bookmark action */ },
                    modifier = Modifier.size(48.dp),
                    containerColor = Color.White,
                    contentColor = MockelloOrange
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Bookmark")
                }
            }
        }
        
        // Main FAB
        FloatingActionButton(
            onClick = { extended = !extended },
            containerColor = MockelloOrange,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 12.dp)
        ) {
            Icon(
                imageVector = if (extended) Icons.Default.Close else Icons.Default.Add,
                contentDescription = "Menu",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
fun AnimatedAboutSection() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(500)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
            initialOffsetY = { it / 3 }
        )
    ) {
        AboutSection()
    }
}

@Composable
fun AboutSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "What We Do",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            color = MockelloDark,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Everything software + AI",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            color = MockelloOrange,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // About description card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                Text(
                    text = "We're a company focused heavily on AI and full-stack development — from building custom AI agents, training/quantizing models, and researching new frameworks, to mobile app development, backend/frontend systems, infra, cloud, and DevOps.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MockelloDark.copy(alpha = 0.9f),
                    lineHeight = 26.sp
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Basically, everything that comes under software + AI, we take it in.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MockelloOrange,
                    lineHeight = 24.sp
                )
            }
        }
    }
}


@Composable
fun AnimatedResearchSection() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)) + slideInHorizontally(
            initialOffsetX = { it }
        )
    ) {
        ResearchSection()
    }
}

@Composable
fun ModernTechStackSection() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(400)
        visible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { -it / 2 })
        ) {
            Column {
                Text(
                    text = "Technology Stack",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center,
                    color = MockelloDark,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Powered by cutting-edge technologies",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MockelloOrange,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Animated tech categories
        val techCategories = listOf(
            "AI & ML" to listOf("TensorFlow", "PyTorch", "Transformers", "LangChain"),
            "Mobile" to listOf("React Native", "Flutter", "Kotlin", "Swift"),
            "Backend" to listOf("Node.js", "Python", "Go", "Rust"),
            "Cloud" to listOf("AWS", "GCP", "Azure", "Docker", "Kubernetes")
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(techCategories.withIndex().toList()) { (index, categoryPair) ->
                val (category, technologies) = categoryPair
                AnimatedTechCard(
                    category = category,
                    technologies = technologies,
                    delay = index * 150L
                )
            }
        }
    }
}

@Composable
fun AnimatedTechCard(
    category: String,
    technologies: List<String>,
    delay: Long
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(delay)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ) + fadeIn()
    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MockelloOrange
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                technologies.forEach { tech ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    MockelloOrangeLight,
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tech,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MockelloDark.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun StunningPartnershipSection() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(500)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1200)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    ) {
        PartnershipSection()
    }
}

@Composable
fun ModernCTASection() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(600)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    ) {
        CTASection()
    }
}

@Composable
fun ServicesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Our Services",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            color = MockelloDark,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // AI Services Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ServiceCard(
                title = "AI Agents",
                description = "Custom AI agents tailored to your business needs",
                modifier = Modifier.weight(1f)
            )
            ServiceCard(
                title = "Model Training",
                description = "Training & quantizing models for optimal performance",
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Development Services Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ServiceCard(
                title = "Mobile Apps",
                description = "Cross-platform mobile application development",
                modifier = Modifier.weight(1f)
            )
            ServiceCard(
                title = "Full-Stack",
                description = "Backend, frontend, and cloud infrastructure",
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Infrastructure Row
        ServiceCard(
            title = "Cloud & DevOps",
            description = "Complete infrastructure solutions with modern DevOps practices",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ServiceCard(
    title: String, 
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MockelloOrange
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MockelloDark.copy(alpha = 0.8f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ResearchSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Modern research header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MockelloDark.copy(alpha = 0.95f),
                            MockelloDark.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MockelloOrange,
                                    MockelloOrangeLight
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Text(
                    text = "AI Research",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                
                Text(
                    text = "Active research in cutting-edge AI technologies",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    color = MockelloOrangeLight,
                    lineHeight = 24.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Enhanced research areas
        val researchAreas = listOf(
            ResearchData("Agent Orchestration", "Advanced multi-agent systems and coordination", Icons.Default.Settings, Color(0xFF2196F3)),
            ResearchData("SLMs (Small Language Models)", "Efficient and specialized language models", Icons.Default.Build, Color(0xFF9C27B0)),
            ResearchData("Efficient Frameworks", "Next-generation AI development frameworks", Icons.Default.Star, Color(0xFF4CAF50))
        )
        
        researchAreas.forEachIndexed { index, research ->
            ModernResearchCard(
                research = research,
                delay = index * 150L
            )
            if (index < researchAreas.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class ResearchData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun ModernResearchCard(
    research: ResearchData,
    delay: Long
) {
    var visible by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    
    val cardHeight by animateDpAsState(
        targetValue = if (isExpanded) 140.dp else 120.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "height"
    )
    
    LaunchedEffect(Unit) {
        delay(delay)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ) + fadeIn()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .clip(RoundedCornerShape(20.dp))
                .clickable { isExpanded = !isExpanded },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Accent gradient strip
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    research.accentColor,
                                    research.accentColor.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Research icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        research.accentColor.copy(alpha = 0.2f),
                                        research.accentColor.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = research.icon,
                            contentDescription = null,
                            tint = research.accentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(20.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = research.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MockelloDark,
                            maxLines = if (isExpanded) 3 else 2
                        )
                        
                        Text(
                            text = research.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MockelloDark.copy(alpha = 0.8f),
                            lineHeight = 20.sp,
                            maxLines = if (isExpanded) 4 else 2
                        )
                        
                        if (isExpanded) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            research.accentColor.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Active Research",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = research.accentColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                    
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = research.accentColor,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(if (isExpanded) 0f else 0f)
                    )
                }
            }
        }
    }
}

@Composable
fun ResearchCard(title: String, description: String) {
    // Legacy card for compatibility
    ModernResearchCard(
        research = ResearchData(title, description, Icons.Default.Star, MockelloOrange),
        delay = 0L
    )
}

@Composable
fun TechnologyStackSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Technology Stack",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            color = MockelloDark,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Tech categories
        val techCategories = listOf(
            "AI & ML" to listOf("TensorFlow", "PyTorch", "Transformers", "LangChain"),
            "Mobile" to listOf("React Native", "Flutter", "Kotlin", "Swift"),
            "Backend" to listOf("Node.js", "Python", "Go", "Rust"),
            "Cloud" to listOf("AWS", "GCP", "Azure", "Docker", "Kubernetes")
        )
        
        techCategories.chunked(2).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowCategories.forEach { (category, technologies) ->
                    TechStackCard(
                        category = category,
                        technologies = technologies,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty space if odd number of items
                if (rowCategories.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TechStackCard(
    category: String,
    technologies: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MockelloOrange
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            technologies.forEach { tech ->
                Text(
                    text = "• $tech",
                    style = MaterialTheme.typography.bodySmall,
                    color = MockelloDark.copy(alpha = 0.8f),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun PartnershipSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MockelloDark,
                            MockelloDark.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Strategic Partnership",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MockelloOrangeLight,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Stacia Corps",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Our Exclusive Partner",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Together, we're pushing the boundaries of AI and software development",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun CTASection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(MockelloOrange, MockelloOrangeLight)
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ready to Build the Future?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Let's create something amazing with AI + software",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /* TODO: Handle contact */ },
                    modifier = Modifier
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MockelloOrange
                    )
                ) {
                    Text(
                        text = "Let's Talk",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

// Hamburger Menu Component
@Composable
fun HamburgerMenu(
    onNavigateToPortal: (Screen) -> Unit,
    onNavigateHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(MockelloOrange, MockelloOrangeLight)
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mockello",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.White
                )
                Text(
                    text = "AI + Full-Stack",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Portal Options
        PortalMenuItem(
            title = "Client Portal",
            description = "Track your projects and progress",
            icon = Icons.Default.Person,
            onClick = { onNavigateToPortal(Screen.CLIENT_PORTAL) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PortalMenuItem(
            title = "Employer Portal",
            description = "Manage hiring and teams",
            icon = Icons.Default.Build,
            onClick = { onNavigateToPortal(Screen.EMPLOYER_PORTAL) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PortalMenuItem(
            title = "Partner Portal",
            description = "Partnership management",
            icon = Icons.Default.Star,
            onClick = { onNavigateToPortal(Screen.PARTNER_PORTAL) }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Home Button
        OutlinedButton(
            onClick = onNavigateHome,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MockelloOrange
            )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back to Home")
        }
    }
}

@Composable
fun PortalMenuItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MockelloOrange.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MockelloOrange,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MockelloDark
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MockelloDark.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// Client Portal Screen
@Composable
fun ClientPortalScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(MockelloOrange, MockelloOrangeLight)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Client Portal",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MockelloDark,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Access your project dashboard, track progress, and view project details",
            style = MaterialTheme.typography.bodyLarge,
            color = MockelloDark.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MockelloOrange
            )
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Login to Portal",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

// Client Login Screen
@Composable
fun ClientLoginScreen(
    onLoginSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // Logo
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(MockelloOrange, MockelloOrangeLight)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Client Login",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MockelloDark
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Access your project dashboard",
            style = MaterialTheme.typography.bodyMedium,
            color = MockelloDark.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Login Button
        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    // Simulate login
                    onLoginSuccess(username)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = username.isNotBlank() && password.isNotBlank() && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MockelloOrange
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Demo credentials info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MockelloOrange.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Demo Credentials",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MockelloOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Username: demo\nPassword: demo123",
                    style = MaterialTheme.typography.bodySmall,
                    color = MockelloDark.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Client Dashboard Screen
@Composable
fun ClientDashboardScreen(
    username: String,
    modifier: Modifier = Modifier
) {
    val projects = remember {
        listOf(
            Project("1", "E-commerce Platform", "Modern React-based shopping platform with AI recommendations", ProjectStatus.COMPLETED, 1.0f, "2024-01-15", "2024-03-20", listOf("React", "Node.js", "MongoDB", "AI/ML")),
            Project("2", "Mobile Banking App", "Secure banking application with biometric authentication", ProjectStatus.ONGOING, 0.75f, "2024-02-01", null, listOf("React Native", "PostgreSQL", "Security")),
            Project("3", "AI Chatbot Platform", "Intelligent customer service chatbot with NLP capabilities", ProjectStatus.ONGOING, 0.45f, "2024-03-10", null, listOf("Python", "TensorFlow", "NLP", "Docker")),
            Project("4", "Smart Dashboard", "Real-time analytics dashboard for business intelligence", ProjectStatus.UPCOMING, 0.0f, "2024-04-15", null, listOf("Vue.js", "D3.js", "WebSocket", "Analytics"))
        )
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Welcome Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(MockelloOrange, MockelloOrangeLight)
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Welcome back, $username!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Track your projects and view progress updates",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Project Stats
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            item {
                ProjectStatsCard(
                    title = "Completed",
                    count = projects.count { it.status == ProjectStatus.COMPLETED },
                    color = Color(0xFF4CAF50)
                )
            }
            item {
                ProjectStatsCard(
                    title = "Ongoing",
                    count = projects.count { it.status == ProjectStatus.ONGOING },
                    color = MockelloOrange
                )
            }
            item {
                ProjectStatsCard(
                    title = "Upcoming",
                    count = projects.count { it.status == ProjectStatus.UPCOMING },
                    color = Color(0xFF2196F3)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Projects List
        Text(
            text = "Your Projects",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MockelloDark
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        projects.forEach { project ->
            ProjectCard(project = project)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProjectStatsCard(
    title: String,
    count: Int,
    color: Color
) {
    Card(
        modifier = Modifier.width(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MockelloDark.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProjectCard(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MockelloDark,
                    modifier = Modifier.weight(1f)
                )
                
                ProjectStatusBadge(status = project.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MockelloDark.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress Bar
            if (project.status == ProjectStatus.ONGOING || project.status == ProjectStatus.COMPLETED) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.bodySmall,
                            color = MockelloDark.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${(project.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MockelloOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = project.progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MockelloOrange,
                        trackColor = MockelloOrange.copy(alpha = 0.2f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Technologies
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(project.technologies) { tech ->
                    Box(
                        modifier = Modifier
                            .background(
                                MockelloOrange.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tech,
                            style = MaterialTheme.typography.bodySmall,
                            color = MockelloOrange,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Start Date",
                        style = MaterialTheme.typography.bodySmall,
                        color = MockelloDark.copy(alpha = 0.7f)
                    )
                    Text(
                        text = project.startDate,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MockelloDark
                    )
                }
                
                if (project.endDate != null) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "End Date",
                            style = MaterialTheme.typography.bodySmall,
                            color = MockelloDark.copy(alpha = 0.7f)
                        )
                        Text(
                            text = project.endDate,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MockelloDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectStatusBadge(status: ProjectStatus) {
    val (color, text) = when (status) {
        ProjectStatus.COMPLETED -> Color(0xFF4CAF50) to "Completed"
        ProjectStatus.ONGOING -> MockelloOrange to "Ongoing"
        ProjectStatus.UPCOMING -> Color(0xFF2196F3) to "Upcoming"
    }
    
    Box(
        modifier = Modifier
            .background(
                color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

// Coming Soon Screen
@Composable
fun ComingSoonScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(MockelloOrange.copy(alpha = 0.2f), MockelloOrange.copy(alpha = 0.1f))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = MockelloOrange,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = MockelloDark,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Coming Soon",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MockelloOrange,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "We're working hard to bring you this portal. Stay tuned for updates!",
            style = MaterialTheme.typography.bodyLarge,
            color = MockelloDark.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    MockelloTheme {
        LandingPage()
    }
}