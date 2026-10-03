package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ArtSpaceApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Modèle de données d'une œuvre
data class Artwork(
    @DrawableRes val imageRes: Int,
    val title: String,
    val artist: String,
    val year: String
)

private val artworks = listOf(
    Artwork(R.drawable.art1, "The Walk Woman With A Parasol", "Claude Monet", "1875"),
    Artwork(R.drawable.art2, "Magritte pops", "Rene Magritte", "1954"),
    Artwork(R.drawable.art3, "Mona Lisa", "Leonardo Da Vinci", "1503")
)

@Composable
fun ArtSpaceApp(modifier: Modifier = Modifier) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val artwork = artworks[currentIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // A : cadre de l'image (centré, avec ombre)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            ArtworkWall(imageRes = artwork.imageRes)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // B : titre + artiste (année)
        ArtworkDescriptor(
            title = artwork.title,
            artist = artwork.artist,
            year = artwork.year
        )

        Spacer(modifier = Modifier.height(24.dp))

        // C : boutons aux deux extrémités
        DisplayController(
            onPrevious = {
                currentIndex = if (currentIndex == 0) artworks.lastIndex else currentIndex - 1
            },
            onNext = {
                currentIndex = if (currentIndex == artworks.lastIndex) 0 else currentIndex + 1
            }
        )
    }
}

@Composable
fun ArtworkWall(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Composable
fun ArtworkDescriptor(
    title: String,
    artist: String,
    year: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(artist)
                }
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    append(" ($year)")
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DisplayController(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onPrevious,
            modifier = Modifier.width(130.dp)
        ) {
            Text("Previous")
        }
        Button(
            onClick = onNext,
            modifier = Modifier.width(130.dp)
        ) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun ArtSpaceTabletPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePhonePreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}