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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.mockello.ui.theme.MockelloTheme
import com.example.mockello.ui.theme.MockelloOrange
import com.example.mockello.ui.theme.MockelloOrangeLight
import com.example.mockello.ui.theme.MockelloDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MockelloTheme {
                LandingPage()
            }
        }
    }
}

@Composable
fun LandingPage() {
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
fun FloatingElements(modifier: Modifier = Modifier) {
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

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    MockelloTheme {
        LandingPage()
    }
}