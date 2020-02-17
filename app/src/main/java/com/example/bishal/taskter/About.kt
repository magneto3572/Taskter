package com.example.bishal.taskter

import android.content.Context
import android.net.Uri
import androidx.annotation.NonNull
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.michaelflisar.changelog.ChangelogBuilder


class About : MaterialAboutActivity() {

    override fun getMaterialAboutList(@NonNull p0: Context): MaterialAboutList {
        val card = MaterialAboutCard.Builder()
            .addItem(MaterialAboutTitleItem.Builder()
                .icon(R.mipmap.icon_round)
                .text("Taskter")
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Version")
                .icon(R.drawable.baseline_new_releases_black_18dp)
                .subText("1.0.0")
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("ChangeLog")
                .icon(R.drawable.outline_settings_backup_restore_black_24dp)
                .setOnClickAction {
                    val builder = ChangelogBuilder()
                        .withUseBulletList(true)
                        .withTitle("What's New")
                        .withOkButtonLabel("ok")
                        .buildAndShowDialog(this,false)
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Check For Updates")
                .icon(R.drawable.outline_system_update_black_24dp)
                .build())
            .build()

        val card2 =MaterialAboutCard.Builder()
            .title("Developer")
            .addItem(MaterialAboutActionItem.Builder()
                .text("Tech2Ease")
                .icon(R.drawable.user)
                .subText("India")
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(this, Uri.parse("https://github.com/magneto3572")))
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Contact Us")
                .subText("Say Hello to us")
                .icon(R.drawable.envelope)
                .setOnClickAction(ConvenienceBuilder.createEmailOnClickAction(this,"bishal.singh.3572@gmail.com","Question Regarding Taskter"))
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Youtube")
                .subText("@Tech2Ease")
                .icon(R.drawable.youtube)
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(this, Uri.parse("https://www.youtube.com/channel/UCNX9e7VkwqM3ESP2R8XyX8Q")))
                .build())
            .build()

        val card3 =MaterialAboutCard.Builder()
            .title("Contributors")
            .addItem(MaterialAboutActionItem.Builder()
                .text("logo_redefined")
                .icon(R.drawable.user)
                .subText("Icon Designer")
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(this, Uri.parse("https://www.instagram.com/logo_redefined/?hl=en")))
                .build())
            .build()
        val card4 = MaterialAboutCard.Builder()
            .title("Open Sourced Libs Used")
            .addItem(MaterialAboutActionItem.Builder()
                .icon(R.drawable.github)
                .text("CircleSeekbar")
                .subText("By Feeeei")
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(this, Uri.parse("https://github.com/feeeei/CircleSeekbar")))
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .icon(R.drawable.github)
                .text("Lottie")
                .subText("By Airbnb")
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(this, Uri.parse("https://github.com/airbnb/lottie-android")))
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .icon(R.drawable.github)
                .text("Material About")
                .subText("By Daniel Stone")
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(this, Uri.parse("https://github.com/daniel-stoneuk")))
                .build())
            .build()
        return MaterialAboutList.Builder()
            .addCard(card)
            .addCard(card2)
            .addCard(card3)
            .addCard(card4)
            .build()
    }

    override fun getActivityTitle(): CharSequence {
        return getString(R.string.mal_title_about)
    }
}
