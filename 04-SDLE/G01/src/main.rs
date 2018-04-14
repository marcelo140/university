#![feature(iterator_step_by)]

extern crate petgraph;
extern crate rand;
extern crate clap;
extern crate criterion_plot as plot;

use petgraph::{Graph, Undirected};
use petgraph::graph::NodeIndex;
use petgraph::algo::connected_components;
use petgraph::dot::{Dot, Config};

use clap::{App, Arg, SubCommand};
use rand::Rng;
use plot::prelude::*;
use std::str::FromStr;

const DEFAULT_NODES: usize = 20;
const AVG_REPETITIONS: usize = 15;
const STEPS: usize = 10;

fn build_connected_graph(nodes: usize) -> Graph<usize, (), Undirected> {
    let mut graph = Graph::<usize, (), Undirected>::new_undirected();

    for n in 0..nodes {
        graph.add_node(n);
    }

    let indices: Vec<NodeIndex> = graph.node_indices().collect();
    let mut rnd = rand::thread_rng();

    while connected_components(&graph) > 1 {
        let a = rnd.choose(&indices).unwrap();
        let b = rnd.choose(&indices).unwrap();

        graph.update_edge(*a, *b, ());
    }

    return graph;
}

fn average_edges_for(nodes: usize) -> f64 {
    let mut acc = 0;

    for _ in 0..AVG_REPETITIONS {
        acc += build_connected_graph(nodes).edge_count();     
    }

    (acc as f64) / (AVG_REPETITIONS as f64)
}

fn plot_averages(nodes: usize) {
    let ref xs: Vec<usize> = (10..nodes).step_by(STEPS).collect();

    Figure::new()
		.configure(Axis::BottomX, |a| {
			a.set(Label("Number of nodes"))
			 .configure(Grid::Major, |g| g.show())
		})
		.configure(Axis::LeftY, |a| {
			a.set(Label("Number of edges"))
			 .configure(Grid::Major, |g| g.show())
		})
		.plot(LinesPoints {
			x: xs,
			y: xs.iter().map(|x| average_edges_for(*x))
		}, |lp| {
			lp.set(PointType::FilledCircle)
			  .set(PointSize(0.6))
		})
		.draw()
		.ok()
		.and_then(|gnuplot| {
			gnuplot.wait_with_output().ok().and_then(|p| String::from_utf8(p.stderr).ok())
		});
}

fn main() {
    let matches = App::new("G01")
                          .about("Allows some sampling with connected graphs")
                          .arg(Arg::with_name("N")
                                .global(true)
                                .help("Number of nodes"))
                          .subcommand(SubCommand::with_name("generate"))
                          .subcommand(SubCommand::with_name("average"))
                          .subcommand(SubCommand::with_name("plot"))
                          .get_matches();


    let nodes = matches.value_of("N")
        .and_then(|s| usize::from_str(s).ok())
        .unwrap_or(DEFAULT_NODES);

    if matches.is_present("plot") {
        plot_averages(nodes+10);
    } else if matches.is_present("average") {
        println!("Average edges for {}: {}", nodes, average_edges_for(nodes));
    } else {
        let graph = build_connected_graph(nodes);
        println!("{:?}", Dot::with_config(&graph, &[Config::EdgeNoLabel]));
    }
}
